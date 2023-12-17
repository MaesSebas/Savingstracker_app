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

class Overall__FragmentInsertTransactionViewModel(var dao: Database__TaskDao) : ViewModel() {
    private val _insertionSuccess = MutableLiveData<Boolean>()
    val insertionSuccess: LiveData<Boolean>
        get() = _insertionSuccess

    fun insertCardTransaction(AccountId: Long, CompanyName: String, Description: String, TransactionDate: String, Category: String, Amount: Double, Type: String) {

        val random = Random()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateBuy = dateFormat.format(Date())
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateBuy)
        calendar.add(Calendar.DAY_OF_MONTH, random.nextInt(30) + 1)
        dateFormat.format(calendar.time)

        val transaction = Database__Transaction(
            userID = 1,
            accountID = AccountId,
            companyName = CompanyName,
            description = Description,
            transactionDate = TransactionDate,
            category = Category,
            amount = Amount,
            type = Type
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var account = dao.getAccountById(AccountId)
                account.totalBalance = changeTotalAmount(Type, account.totalBalance.toDouble(), Amount).toString()
                dao.update(account)
                val transaction = Database__Transaction(
                    userID = 1,
                    accountID = AccountId,
                    companyName = CompanyName,
                    description = Description,
                    transactionDate = TransactionDate,
                    category = Category,
                    amount = Amount,
                    type = Type
                )
                dao.insert(transaction)
                _insertionSuccess.postValue(true)
            }
        }
    }

    fun changeTotalAmount(expenseOrIncome: String, oldAmount: Double, transactionAmount: Double): Double {
        if(expenseOrIncome == "Income") {
            return transactionAmount + oldAmount
        } else {
            return oldAmount - transactionAmount
        }
    }

    fun resetInsertionSuccess() {
        _insertionSuccess.value = false
    }
}