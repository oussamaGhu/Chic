package tn.esprit.chiccercle.presentation.auth.forgetPassword

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ForgetPasswordViewModel : ViewModel() {
    var email = mutableStateOf("")
        private set

    fun onEmailChanged(newEmail: String) {
        email.value = newEmail
    }

    fun sendMail() {
        // Implement your logic to send a password reset email
    }
}