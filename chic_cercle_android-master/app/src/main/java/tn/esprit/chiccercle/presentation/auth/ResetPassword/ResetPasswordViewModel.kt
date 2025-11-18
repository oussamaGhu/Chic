package tn.esprit.chiccercle.presentation.auth.ResetPassword


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ResetPasswordViewModel : ViewModel() {
    var newPassword = mutableStateOf("")
        private set

    var confirmPassword = mutableStateOf("")
        private set

    fun onNewPasswordChanged(newPassword: String) {
        this.newPassword.value = newPassword
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        this.confirmPassword.value = confirmPassword
    }

    fun savePassword() {
        // Implement your logic to save the new password
        // Ensure that newPassword and confirmPassword match before saving
    }
}