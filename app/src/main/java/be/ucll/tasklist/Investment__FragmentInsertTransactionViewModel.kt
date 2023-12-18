package be.ucll.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class Investment__FragmentInsertTransactionViewModel(var dao: Database__TaskDao)  : ViewModel() {
    private val _insertionSuccess = MutableLiveData<Boolean>()
    val insertionSuccess: LiveData<Boolean>
        get() = _insertionSuccess

    fun insertAssetTransaction(investmentType: String, nameAsset: String, tickerAsset: String, quantityAsset: Int, valueAsset: Double, accountId: Long) {

        val random = Random()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateBuy = dateFormat.format(Date())
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateBuy)
        calendar.add(Calendar.DAY_OF_MONTH, random.nextInt(30) + 1)
        dateFormat.format(calendar.time)

        val assetToInsert = Database__Asset(
            userID= 0,
            investmentType= investmentType,
            name= nameAsset,
            ticker= tickerAsset,
            quantity= quantityAsset,
            lastValue= valueAsset,
            lastUpdated= dateFormat.format(calendar.time)
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var account = dao.getAccountById(accountId)
                account.totalBalance = (account.totalBalance.toDouble() + valueAsset.toDouble() * quantityAsset.toDouble()).toString()
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