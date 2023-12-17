package be.ucll.tasklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class Savingsaccounts__FragmentSavingsViewModel(var dao: Database__TaskDao) : ViewModel() {
    //var dataListLiveData: MutableLiveData<List<Database__CustomData>> = MutableLiveData()
    var totalSavingsAmount: MutableLiveData<Double> = MutableLiveData(0.0)
    var checkingsAccountLiveData: MutableLiveData<List<Database__AccountsAndTransactions>> = MutableLiveData()
    var graphLiveData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        viewModelScope.launch {
            val assetData = withContext(Dispatchers.IO) {
                dao.getAccountsWithTransactions("SavingsAccount")
            }
            checkingsAccountLiveData.postValue(assetData)
            generateGraphDataOutOfMockData(assetData)
            calculateTotalBalance(assetData)
        }
    }

    fun addMonthDeviders(data: List<Database__AccountsAndTransactions>): List<Database__AccountsAndTransactions> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        for (account in data) {
            val transactions = account.transactions.toMutableList()
            var previousMonth = ""

            for (i in transactions.indices) {
                val currentTransaction = transactions[i]
                val currentMonth = LocalDate.parse(currentTransaction.transactionDate, formatter).month.toString()
                if (previousMonth != currentMonth) {
                    val newTransaction = Database__Transaction(
                        transactionID= 1,
                        userID= 1,
                        accountID= 1,
                        companyName= "Divider",
                        description= "Divider",
                        transactionDate= currentMonth,
                        category= "Divider",
                        amount= 999999.00,
                        type= "Divider"
                    )
                    transactions.add(i, newTransaction)
                }
                previousMonth = currentMonth
            }
            account.transactions = transactions
        }
        return data
    }

    fun calculateTotalBalance(Data: List<Database__AccountsAndTransactions>) {
        Data?.let { list ->
            var currentTotalAmount = 0.0
            for (checkingsAccountData in list) {
                currentTotalAmount += checkingsAccountData.account.totalBalance.toDouble()
            }
            totalSavingsAmount.postValue(currentTotalAmount)
        }
    }

    fun generateGraphDataOutOfMockData(checkingsAccountData: List<Database__AccountsAndTransactions>) {
        //val checkingsAccountData = checkingsAccountLiveData.value ?: return
        val currentTotalAmount = 1000.0 // You should replace this with the actual current total amount
        val groupedDataPerDay = groupTransactionsByDay(checkingsAccountData)
        val filteredTransactionsToLast30Days = filterTransactionsForLast30Days(groupedDataPerDay)
        val totalAmountDataPerDay = calculateTotalAmountPerDay(currentTotalAmount, filteredTransactionsToLast30Days)
        val test = convertDataToGraphData(totalAmountDataPerDay)
        graphLiveData.value = test
    }

    fun convertDataToGraphData(totalAmountDataPerDay: List<Pair<String, String>>): List<String> {
        return totalAmountDataPerDay.map { it.second }
    }

    fun filterTransactionsForLast30Days(data: List<Pair<String, List<Database__Transaction>>>): List<Pair<String, List<Database__Transaction>>> {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()

        val filteredData = data.mapNotNull { (key, transactions) ->
            val transactionDate = dateFormatter.parse(key) // Parse the date from the string key

            if (transactionDate != null) {
                val daysDifference = ((currentDate.time - transactionDate.time) / (1000 * 60 * 60 * 24)).toInt()
                if (daysDifference <= 30) {
                    key to transactions
                } else {
                    null
                }
            } else {
                key to transactions
            }
        }

        return filteredData
    }

    fun calculateTotalAmountPerDay(currentTotalAmount: Double, data: List<Pair<String, List<Database__Transaction>>>) : List<Pair<String, String>> {
        var totalAmount = currentTotalAmount
        val result = mutableListOf<Pair<String, String>>()

        for ((day, transactions) in data) {
            val dayTotal = transactions.fold(0.0) { acc, transaction ->
                val transactionAmount = if (transaction.type == "Deposit") {
                    transaction.amount
                } else if (transaction.type == "Expense") {
                    -transaction.amount
                } else {
                    0.0 // Handle other types as needed
                }
                acc + transactionAmount
            }

            totalAmount += dayTotal
            result.add(day to totalAmount.toString())
        }

        return result
    }

    fun groupTransactionsByDay(data: List<Database__AccountsAndTransactions>?): List<Pair<String, List<Database__Transaction>>> {
        data ?: return emptyList()

        val transactionsByDay = data
            .flatMap { it.transactions.orEmpty() } // Handle null transactions

        val grouped = transactionsByDay
            .groupBy { transaction ->
                transaction.transactionDate?.substring(0, 10)
            }
            .mapNotNull { (day, transactions) ->
                day?.let { it to transactions }
            }

        return grouped
    }
}