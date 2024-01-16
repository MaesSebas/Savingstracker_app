package be.ucll.tasklist

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.atomic.AtomicInteger

data class Error(val investmentId: Long, val accountId: Long, val errorMessage: String)
class Investment__FragmentInvestmentViewModel(var dao: Database__TaskDao) : ViewModel() {
    var investmentsLiveData: MutableLiveData<List<DatabaseAssetAndTransactions>> = MutableLiveData()
    var graphLiveData: MutableLiveData<List<Double>> = MutableLiveData()
    var totalCardAmount: MutableLiveData<Double> = MutableLiveData(0.0)
    var accountLiveData: MutableLiveData<List<Database__AssetAccountsAndAssets>> = MutableLiveData()

    var errorBuffer: MutableList<Error> = mutableListOf()
    init {
        viewModelScope.launch {
            val assetData = withContext(Dispatchers.IO) {
                dao.getAssetsWithTransactions()
            }
            val accountData = withContext(Dispatchers.IO) {
                dao.getAssetAccountWithTransaction()
            }
            getTotalBalanceAccount(accountData[0])
            accountLiveData.value = accountData
            investmentsLiveData.value = assetData
            graphLiveData.value = generateGraphData(assetData)


            val apiData = filterAsetDataForAPI(assetData);
            if(apiData.isEmpty()) {
                return@launch
            }
            fetchDataFromApi(apiData) { assetDataList ->
                if (assetDataList.isNotEmpty()) {
                    viewModelScope.launch {
                        updateDatabase(assetDataList)
                    }
                } else {
                    // Handle the case when the list is empty
                }
                viewModelScope.launch {
                    handleErrors()
                }
            }
        }
    }

    suspend fun handleErrors() {
        for (error in errorBuffer) {
            withContext(Dispatchers.IO) {
                val asset = dao.getAssetById(error.investmentId)
                asset.lastUpdated = LocalDate.now().toString()
                asset.errorTickerNotFound = true
                dao.update(asset)
            }
        }
    }

    suspend fun updateDatabase(newData: List<Database__PriceAsset>) {
        withContext(Dispatchers.IO) {
            val uniqueIds = newData.map { it.investmentId }.distinct()
            for (id in uniqueIds) {
                dao.deleteOldEntryById(id)
            }
            for (entry in newData) {
                dao.insert(entry)
            }
            for (id in uniqueIds) {
                val lastUpdatedDate = newData.filter { it.investmentId == id }
                    .maxByOrNull { it.dateOfPrice }?.dateOfPrice
                val Asset = dao.getAssetById(id)
                val closePriceString = newData.filter { it.investmentId == id }
                    .maxByOrNull { it.dateOfPrice }?.closePrice ?: ""

                Asset.lastValue = closePriceString.toDoubleOrNull() ?: Asset.lastValue
                Asset.lastValue = Asset.quantity.toDouble() * Asset.lastValue
                Asset.lastUpdated = lastUpdatedDate as String
                dao.update(Asset)
            }
        }
    }

    fun filterAsetDataForAPI(allInvestments: List<DatabaseAssetAndTransactions>): List<DatabaseAssetAndTransactions> {
        val today = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var test =  allInvestments.filter {
            it.asset.investmentType == "Stocks" &&
                    LocalDate.parse(it.asset.lastUpdated, dateFormatter).isBefore(today)
        } //.take(2)
        return test
    }

    fun getInvestmentsDatPerType(allInvestments: MutableLiveData<List<DatabaseAssetAndTransactions>>, type: String): DatabaseParcableAssetAndTransactions {
        val filteredList = allInvestments.value?.filter { it.asset.investmentType == type }
        return DatabaseParcableAssetAndTransactions(filteredList!!)
    }

    fun getTotalBalanceAccount(accountData: Database__AssetAccountsAndAssets) {
        totalCardAmount.value = accountData.account.totalBalance.toDouble()
    }

    fun generateGraphData(data: List<DatabaseAssetAndTransactions>): List<Double> {
        val filteredData = getAsset("All", data)

        val assets30DaysData = mutableListOf<List<Double>>()
        for (asset in filteredData) {
            val quantitiesPerDay = calculateQuantityPerDay(asset)
            val valuesPerDay = getDataPerDayFromAssetAPI()
            val assetData = calculateValueAssetsPerDay(quantitiesPerDay, valuesPerDay)
            assets30DaysData.add(assetData)
        }
       return countAssetsTogether(assets30DaysData)
    }

    fun countAssetsTogether(list: MutableList<List<Double>>): List<Double> {
        val totalList = MutableList(30) { 0.0 }
        for (assetDataList in list) {
            for (i in assetDataList.indices) {
                totalList[i] += assetDataList[i]
            }
        }
        return totalList
    }

    fun getAsset(filterType: String, assetData: List<DatabaseAssetAndTransactions>): List<DatabaseAssetAndTransactions> {
        //filterType not implementedYet
        return assetData
    }

    fun calculateQuantityPerDay(data: DatabaseAssetAndTransactions): List<Int> {
        val currentDate = "2023-11-29" // Current date or the date you want to calculate quantities for
        val thirtyDaysAgo = LocalDate.parse(currentDate).minusDays(29).toString()

        val transactions = data.transactions.filter {
            it.transactionDate >= thirtyDaysAgo && it.transactionDate <= currentDate
        }.sortedBy { it.transactionDate }

        var currentQuantity = data.asset.quantity
        val quantitiesList = mutableListOf<Int>()

        for (i in 0 until 30) {
            val date = LocalDate.parse(currentDate).minusDays(i.toLong()).toString()
            val transaction = transactions.find { it.transactionDate == date }

            if (transaction != null) {
                currentQuantity += when (transaction.transactionType) {
                    "buy" -> transaction.quantity
                    "sell" -> -transaction.quantity
                    else -> 0
                }
            }

            quantitiesList.add(currentQuantity)
        }

        val test = quantitiesList.reversed()
        return test
    }

    fun getDataPerDayFromAssetAPI(): List<Double> {
        val randomNumbers = mutableSetOf<Double>()
        while (randomNumbers.size < 30) {
            val randomNumber = (100..999).random()
            randomNumbers.add(randomNumber.toDouble())
        }
        return randomNumbers.toList()
    }

    fun calculateValueAssetsPerDay(quantities: List<Int>, prices: List<Double>): List<Double> {
        if (quantities.size != prices.size) {
            throw IllegalArgumentException("quantities and prices must have the same size")
        }

        val valuesPerDay = mutableListOf<Double>()
        for (i in 0 until quantities.size) {
            val valuePerDay = quantities[i].toDouble() * prices[i]
            valuesPerDay.add(valuePerDay)
        }

        return valuesPerDay
    }


    fun fetchDataFromApi(assets: List<DatabaseAssetAndTransactions>, callback: (List<Database__PriceAsset>) -> Unit): List<Database__PriceAsset> {
        val apiKey = "LAH58AZFLBLNGVH0"
        val function = "TIME_SERIES_DAILY"
        val outputSize = "30"
        val allAssetTimeData = mutableListOf<Database__PriceAsset>()

        val assetsFetched = AtomicInteger(0)

        for (asset in assets) {
            val apiUrl = "https://www.alphavantage.co/query?function=$function&symbol=${asset.asset.ticker}&outputsize=$outputSize&apikey=$apiKey"

            FetchDataTask(asset.asset.investmentId, asset.asset.accountID) { assetTimeData ->
                allAssetTimeData.addAll(assetTimeData)

                // Increment the counter and check if all assets have been fetched
                if (assetsFetched.incrementAndGet() == assets.size) {
                    callback(allAssetTimeData)
                }
            }.execute(apiUrl)
        }

        // Return an empty list since the actual data will be provided via the callback
        return emptyList()
    }

    private inner class FetchDataTask(
        private val investmentId: Long,
        private val accountId: Long,
        private val callback: (List<Database__PriceAsset>) -> Unit
    ) : AsyncTask<String, Void, JSONObject?>() {

        override fun doInBackground(vararg params: String): JSONObject? {
            val apiUrl = params[0]

            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    return JSONObject(response.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: JSONObject?) {
            if (result?.has("Error Message") == true) {
                errorCallback(investmentId, accountId)
            } else if (result?.has("Information") == true && result.getString("Information").contains("api rate limit", ignoreCase = true)) {
                return
            } else if (result != null) {
                handleApiResponse(result, investmentId, accountId, callback)
            } else {
                callback(emptyList())
            }
        }

        private fun errorCallback(investmentId: Long, accountId: Long) {
            errorBuffer.add(Error(investmentId, accountId, "error handling API, Ticker not found"))
        }

        private fun handleApiResponse(response: JSONObject, investmentId: Long, accountId: Long, callback: (List<Database__PriceAsset>) -> Unit) {
            val rawTimeDataOfAsset = response.getJSONObject("Time Series (Daily)")
            val assetTimeData = parseTimeData(rawTimeDataOfAsset, investmentId, accountId)
            callback(assetTimeData)
        }

        private fun parseTimeData(timeSeries: JSONObject, investmentId: Long, accountId: Long): List<Database__PriceAsset> {
            val dailyDataList = mutableListOf<Database__PriceAsset>()
            for (date in timeSeries.keys()) {
                val dailyDataObject = timeSeries.getJSONObject(date)
                val dailyData = Database__PriceAsset(
                    investmentId = investmentId,
                    accountID = accountId,
                    dateOfPrice = date,
                    openPrice = dailyDataObject.getString("1. open"),
                    highPrice = dailyDataObject.getString("2. high"),
                    lowPrice = dailyDataObject.getString("3. low"),
                    closePrice = dailyDataObject.getString("4. close"),
                    volume = dailyDataObject.getString("5. volume")
                )
                dailyDataList.add(dailyData)
            }
            return dailyDataList
        }
    }
}