package be.ucll.tasklist

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate



/*
data class AssetData(
    val information: String,
    val symbol: String,
    val lastRefreshed: String,
    val outputSize: String,
    val timeZone: String,
    var Prices: List<PriceAsset>,
    var error: String,
)

@Parcelize
@Entity(tableName = "investment_priceAsset_table")
data class Database__PriceAsset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val investmentId: Long,
    val accountID: Long,
    val dateOfPrice: String,
    val openPrice: String,
    val closePrice: String,
    val highPrice: String,
    val lowPrice: String,
    val volume: String
): Parcelable

data class PriceAsset(
    val date: String,
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val volume: String
)

 */

class Investment__FragmentInvestmentViewModel(var dao: Database__TaskDao) : ViewModel() {
    var investmentsLiveData: MutableLiveData<List<DatabaseAssetAndTransactions>> = MutableLiveData()
    var graphLiveData: MutableLiveData<List<Double>> = MutableLiveData()
    var totalCardAmount: MutableLiveData<Double> = MutableLiveData(0.0)
    var accountLiveData: MutableLiveData<List<Database__AssetAccountsAndAssets>> = MutableLiveData()
    private var assetDataFromAPI: MutableList<Database__PriceAsset> = mutableListOf()

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

            //fetchDataFromApi(assetData)
        }
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
        var test = countAssetsTogether(assets30DaysData)
        return test
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

    fun calculateQuantityOnDate(investment: DatabaseAssetAndTransactions, date: String): Int {
        var quantityOnDate = investment.asset.quantity

        for (transaction in investment.transactions) {
            if (transaction.transactionDate <= date) {
                when (transaction.transactionType) {
                    "buy" -> quantityOnDate += transaction.quantity
                    "sell" -> quantityOnDate -= transaction.quantity
                }
            }
        }

        return quantityOnDate
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

    fun fetchDataFromApi(assets: List<DatabaseAssetAndTransactions>) {
        val apiKey = "LAH58AZFLBLNGVH0"
        val function = "TIME_SERIES_DAILY"
        val outputSize = "30"

        for (asset in assets) {
            val apiUrl =
                "https://www.alphavantage.co/query?function=$function&symbol=${asset.asset.ticker}&outputsize=$outputSize&apikey=$apiKey"
            FetchDataTask(asset.asset.investmentId, asset.asset.accountID).execute(apiUrl)
            return
        }
    }

    private inner class FetchDataTask(private val investmentId: Long, private val accountId: Long) : AsyncTask<String, Void, JSONObject?>() {

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
            if (result != null) {
                handleApiResponse(result, investmentId, accountId)
            } else {
                val test = "test"
            }
        }
    }

    private fun handleApiResponse(response: JSONObject, investmentId: Long, accountId: Long) {
        //val rawMetaDataOfAset = response.getJSONObject("Meta Data")
        val rawTimeDataOfAsset = response.getJSONObject("Time Series (Daily)")

        //val assetData = parseOveralData(rawMetaDataOfAset)
        val assetTimeData = parseTimeData(rawTimeDataOfAsset, investmentId, accountId)
        assetDataFromAPI.addAll(assetTimeData)

        //assetData.Prices = assetTimeData
        //assetDataFromAPI.add(assetData)
    }

    /*
    private fun parseOveralData(metaData: JSONObject): AssetData {
        return AssetData(
            information = metaData.getString("1. Information"),
            symbol = metaData.getString("2. Symbol"),
            lastRefreshed = metaData.getString("3. Last Refreshed"),
            outputSize = metaData.getString("4. Output Size"),
            timeZone = metaData.getString("5. Time Zone"),
            Prices = emptyList(),
            error = "OK",
        )
    }
     */

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