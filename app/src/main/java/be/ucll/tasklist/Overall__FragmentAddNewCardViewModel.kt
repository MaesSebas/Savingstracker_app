package be.ucll.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class Overall__FragmentAddNewCardViewModel(var dao: Database__TaskDao) : ViewModel() {
    private val _insertionSuccess = MutableLiveData<Boolean>()
    val insertionSuccess: LiveData<Boolean>
        get() = _insertionSuccess

    fun insertNewAccount(typeAccount: String, startAmount: String, accountName: String, accountnumber: String) {
        val account = Database__Account(
            userID = 1,
            accountName = accountName,
            accountNumber = accountnumber,
            totalBalance = startAmount,
            accountType = typeAccount
        )
        viewModelScope.launch {
            dao.insert(account)
            _insertionSuccess.value = true
        }
    }

    fun resetInsertionSuccess() {
        _insertionSuccess.value = false
    }
}