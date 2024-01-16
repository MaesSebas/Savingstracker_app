package be.ucll.tasklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.lang.Math.round
import java.time.LocalDate

class Investment__FragmentOverviewPerAssetViewModel(var dao: Database__TaskDao) : ViewModel() {
    var transactionsLiveData: MutableLiveData<List<DatabaseAssetAndTransactions>> = MutableLiveData()
    var graphLiveData: MutableLiveData<List<Double>> = MutableLiveData()
    var totalCardAmount: MutableLiveData<Int> = MutableLiveData(0)

    // if we set arguments you need the data first before moving on (viewmodel first initiated so init function comes first here, and then arguments doesnt work) so this serves as init function
    fun setTransactionsDataAndFungateAsInit(data: List<DatabaseAssetAndTransactions>) {
        transactionsLiveData.value = data
        graphLiveData.value = generateGraphData(transactionsLiveData.value!!)

        totalCardAmount.value = getTotalAccount(data)
    }

    fun getTotalAccount(data: List<DatabaseAssetAndTransactions>): Int {
        var totalAmount = 0.0
        for(investment in data) {
            var calculateValue = investment.asset.lastValue * investment.asset.quantity
            totalAmount += calculateValue
        }
        return totalAmount.toInt()
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
}