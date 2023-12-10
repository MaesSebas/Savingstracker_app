package be.ucll.tasklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class Investment__FragmentOverviewPerAssetViewModel(var dao: Database__TaskDao) : ViewModel() {
    var transactionsLiveData: MutableLiveData<List<Database__AssetAndTransactions>> = MutableLiveData()
    var graphLiveData: MutableLiveData<List<Double>> = MutableLiveData()
    
    init {
        viewModelScope.launch {
            val assetData = withContext(Dispatchers.IO) {
                dao.getAllAssetsAndTransactionsByType("Stocks")
            }
            transactionsLiveData.value = assetData
            graphLiveData.value = generateGraphData(assetData)
        }
    }

    fun generateGraphData(data: List<Database__AssetAndTransactions>): List<Double> {
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

    fun getAsset(filterType: String, assetData: List<Database__AssetAndTransactions>): List<Database__AssetAndTransactions> {
        //filterType not implementedYet
        return assetData
    }

    fun calculateQuantityPerDay(data: Database__AssetAndTransactions): List<Int> {
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

    fun calculateQuantityOnDate(investment: Database__AssetAndTransactions, date: String): Int {
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
}