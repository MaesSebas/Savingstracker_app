package be.ucll.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Login__FragmentLoginViewModel : ViewModel() {
    private val presetCode = "1234"

    private val _enteredCode = MutableLiveData<String>()
    val enteredCode: LiveData<String> = _enteredCode

    private val _enteredDigits = MutableLiveData<Int>()
    val enteredDigits: LiveData<Int> = _enteredDigits

    private val _codeIncorrect = MutableLiveData<Boolean>()
    val codeIncorrect: LiveData<Boolean> = _codeIncorrect

    fun appendDigit(digit: String) {
        if ((_enteredCode.value?.length ?: 0) >= 4) {
            return
        }

        _enteredDigits.value = (_enteredDigits.value ?: 0) + 1
        _enteredCode.value = (_enteredCode.value ?: "") + digit
    }

    fun clearCode() {
        _enteredCode.value = ""
        _enteredDigits.value = 0
    }

    fun checkCodeAndRedirectIfCorrect() {
        if (codeIsCorrect()) {
            _codeIncorrect.value = false
        } else {
            _codeIncorrect.value = true
            clearCode()
        }
    }

    fun codeIsCorrect(): Boolean {
        return enteredCode.value == presetCode
    }
}