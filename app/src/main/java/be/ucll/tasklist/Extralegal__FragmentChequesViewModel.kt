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

class Extralegal__FragmentChequesViewModel(var dao: TaskDao) : ViewModel() {
    var extraLegalCardsLiveData: MutableLiveData<List<Database__C_AccountsAndTransactions>> = MutableLiveData()
    var totalCardAmount: MutableLiveData<Double> = MutableLiveData(0.0)
    var graphLiveData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        viewModelScope.launch {
            val accountData = withContext(Dispatchers.IO) {
                dao.getAccountsWithTransactions("ExtraLegalAccount")
            }
            extraLegalCardsLiveData.postValue(accountData)
            val graphDataFromatter = Overall_ConvertToGraphDataFormat()
            val totalAmount = graphDataFromatter.calculateTotalBalance(accountData)
            totalCardAmount.value = totalAmount
            graphLiveData.value = graphDataFromatter.generateGraphDataOutOfMockData(accountData, totalAmount)
        }
    }
}