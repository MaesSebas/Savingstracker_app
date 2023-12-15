package be.ucll.tasklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Checkingsaccounts__FragmentCardsViewModel(var dao: Database__TaskDao) : ViewModel() {
    var checkingsAccountLiveData: MutableLiveData<List<Database__AccountsAndTransactions>> = MutableLiveData()
    var totalCardAmount: MutableLiveData<Double> = MutableLiveData(0.0)
    var graphLiveData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        viewModelScope.launch {
            var accountData = withContext(Dispatchers.IO) {
                dao.getAccountsWithTransactions("CheckingsAccount")
            }
            accountData += addFakeCardToGivePossibilityToAddNewCard()
            checkingsAccountLiveData.postValue(accountData)
            generateGraphDataOutOfMockData(accountData)
        }
    }

    fun addFakeCardToGivePossibilityToAddNewCard():  Database__AccountsAndTransactions{
        return Database__AccountsAndTransactions(
            account = Database__Account(
                userID = 1,
                accountID = 99,
                accountName = "AddCardFAKE",
                accountNumber = "9999999999",
                totalBalance = "0",
                accountType = "CheckingsAccount"
            ),
            transactions = listOf<Database__Transaction>()
        )
    }

    fun generateGraphDataOutOfMockData(Data: List<Database__AccountsAndTransactions>) {
        calculateTotalBalance(Data)
        val groupedDataPerDay = groupTransactionsByDay(Data)
        val filteredTransactionsToLast30Days = filterTransactionsForLast30Days(groupedDataPerDay)
        val totalAmountDataPerDay = calculateTotalAmountPerDay(totalCardAmount.value!!, filteredTransactionsToLast30Days)
        graphLiveData.value = convertDataToGraphData(totalAmountDataPerDay)
    }

    fun calculateTotalBalance(Data: List<Database__AccountsAndTransactions>) {
        Data?.let { list ->
            var currentTotalAmount = 0.0
            for (checkingsAccountData in list) {
                currentTotalAmount += checkingsAccountData.account.totalBalance.toDouble()
            }
            totalCardAmount.postValue(currentTotalAmount)
        }
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
                    0.0
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
            .flatMap { it.transactions.orEmpty() }

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