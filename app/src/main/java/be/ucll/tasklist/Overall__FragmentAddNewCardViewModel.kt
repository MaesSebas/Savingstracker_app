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

    fun insertCardTransaction(CompanyName: String, Description: String, TransactionDate: String, Category: String, Amount: Double, Type: String) {

        val random = Random()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateBuy = dateFormat.format(Date())
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateBuy)
        calendar.add(Calendar.DAY_OF_MONTH, random.nextInt(30) + 1) // Add 1 to 30 days
        dateFormat.format(calendar.time)

        val transaction = Database__Transaction(
            userID = 1,
            accountID = 3,
            companyName = CompanyName,
            description = Description,
            transactionDate = "2023-12-12",
            category = Category,
            amount = Amount,
            type = Type
        )
        viewModelScope.launch {
            dao.insert(transaction)
            _insertionSuccess.value = true
        }
    }

    fun resetInsertionSuccess() {
        _insertionSuccess.value = false
    }
}