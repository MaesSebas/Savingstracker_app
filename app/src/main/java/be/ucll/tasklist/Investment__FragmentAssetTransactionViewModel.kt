package be.ucll.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class Investment__FragmentAssetTransactionViewModel(var dao: Database__TaskDao) : ViewModel() {
    private val _insertionSuccess = MutableLiveData<Boolean>()
    val insertionSuccess: LiveData<Boolean>
        get() = _insertionSuccess

    fun insertAssetTransaction(expenseOrIncome: String, transactionDate: String, investmenId: Long, quantity: Int, value: Double) {

        val random = Random()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateBuy = dateFormat.format(Date())
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateBuy)
        calendar.add(Calendar.DAY_OF_MONTH, random.nextInt(30) + 1)
        dateFormat.format(calendar.time)

        val assetToInsert = Database__AssetTransaction(
            historyId= 0L,
            userID= 1,
            accountID= 7,
            investmentId= investmenId,
            transactionDate= transactionDate,
            transactionType= expenseOrIncome,
            quantity= quantity,
            value= value
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val account = dao.getAssetById(investmenId)
                account.lastValue = account.lastValue.toDouble() + quantity.toDouble() * value.toDouble()
                dao.update(account)
                dao.insert(assetToInsert)
                _insertionSuccess.postValue(true)
            }
        }
    }

    fun resetInsertionSuccess() {
        _insertionSuccess.value = false
    }
}