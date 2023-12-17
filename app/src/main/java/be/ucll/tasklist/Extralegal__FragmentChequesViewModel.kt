package be.ucll.tasklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Extralegal__FragmentChequesViewModel(var dao: Database__TaskDao) : ViewModel() {
    var extraLegalCardsLiveData: MutableLiveData<List<Database__AccountsAndTransactions>> = MutableLiveData()
    var totalCardAmount: MutableLiveData<Double> = MutableLiveData(0.0)
    var graphLiveData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        viewModelScope.launch {
            var accountData = withContext(Dispatchers.IO) {
                dao.getAccountsWithTransactions("ExtraLegalAccount")
            }
            accountData = sortDataTransactions(accountData)
            accountData += addFakeCardToGivePossibilityToAddNewCard()
            extraLegalCardsLiveData.postValue(accountData)
            val graphDataFromatter = Overall_ConvertToGraphDataFormat()
            val totalAmount = graphDataFromatter.calculateTotalBalance(accountData)
            totalCardAmount.value = totalAmount
            graphLiveData.value = graphDataFromatter.generateGraphDataOutOfMockData(accountData, totalAmount)
        }
    }

    fun sortDataTransactions(data: List<Database__AccountsAndTransactions>): List<Database__AccountsAndTransactions> {
        val sortedData = data.map { account ->
            val sortedTransactions = account.transactions.sortedByDescending { transaction -> transaction.transactionDate }
            account.copy(transactions = sortedTransactions)
        }
        return sortedData
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
}