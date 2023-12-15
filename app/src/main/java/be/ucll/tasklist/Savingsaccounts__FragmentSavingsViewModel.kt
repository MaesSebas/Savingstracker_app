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

    /*
    fun generateMockDataForCheckingsAccount() {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val random = Random()
        val checkingsAccountMockData = mutableListOf<Database__C_AccountsAndTransactions>()

        // First Account
        val account1 = Database__C_Account(
            userID = 1,
            accountName = "ING",
            accountNumber = "BE12345678",
            totalBalance = "1000.00",
            accountType = "CheckingsAccount"
        )

        val transactions1 = mutableListOf<Database__C_Transaction>()

        for (i in 1..80) {
            val transactionDate = dateFormatter.format(Date(System.currentTimeMillis() - (i * 86400000L))) // Generate dates for the past 80 days
            val isDeposit = random.nextBoolean()
            val type = if (isDeposit) "Deposit" else "Expense"

            val transaction = Database__C_Transaction(
                0,
                account1.accountID,
                "Company $i",
                "Description of transaction $i",
                transactionDate,
                "Category $i",
                Math.round(1.0 + random.nextDouble() * 99.0).toDouble(),
                type
            )

            transactions1.add(transaction)
        }

        val checkingsAccountData1 =
            Database__C_AccountsAndTransactions(account1, transactions1)
        checkingsAccountMockData.add(checkingsAccountData1)

        // Second Account
        val account2 = Database__C_Account(
            userID = 2,
            accountName = "KBC",
            accountNumber = "BE87654321",
            totalBalance = "1500.00",
            accountType = "CheckingsAccount"
        )

        val transactions2 = mutableListOf<Database__C_Transaction>()

        for (i in 1..80) {
            val transactionDate = dateFormatter.format(Date(System.currentTimeMillis() - (i * 86400000L))) // Generate dates for the past 80 days

            val transaction = Database__C_Transaction(
                0,
                account2.accountID,
                "Company $i",
                "Description of transaction $i",
                transactionDate,
                "Category $i",
                1.0 + random.nextDouble() * 99.0,
                "Type $i"
            )

            transactions2.add(transaction)
        }

        val checkingsAccountData2 =
            Database__CT_AccountsAndTransactions(account2, transactions2)
        checkingsAccountMockData.add(checkingsAccountData2)
        checkingsAccountLiveData.value = checkingsAccountMockData
    }
     */


    /*
    private fun generateMockData() {
        val customDataList = listOf(
            Database__CustomData("Item 1", "Description 1",
                Database__RecyclerViewData("Title 1", "Description 1")
            ),
            Database__CustomData("Item 2", "Description 2",
                Database__RecyclerViewData("Title 2", "Description 2")
            )
        )
        dataListLiveData.value = customDataList
    }
     */

    /*
    private fun updateDataList() {
        viewModelScope.launch {
            val accounts = withContext(Dispatchers.IO) {
                dao.getAllAccountsByType("checkingAccount")
            }
            val dataList = ArrayList<MyData>()
            for (account in accounts) {
                val myData = MyData(account.accountName, account.accountType)
                dataList.add(myData)
            }
            //dataListLiveData.value = dataList
        }
    }
     */
}