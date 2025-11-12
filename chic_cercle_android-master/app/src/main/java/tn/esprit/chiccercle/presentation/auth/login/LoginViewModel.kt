package tn.esprit.chiccercle.presentation.auth.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.data.network.LoginRequest
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.data.persistence.SessionManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val snackbarMessage = mutableStateOf("")

    private val sessionManager = SessionManager()

    fun login(onSuccess: () -> Unit, onError:  (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = MyRetrofit.userAPI.login(LoginRequest(email.value, password.value))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        sessionManager.saveUserId(body.userId) // Save String userId
                        sessionManager.saveAccessToken(body.accessToken)
                        sessionManager.saveRole(body.role)

                        // Switch to the main thread to call onSuccess
                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                    } else {
                        // Switch to the main thread to call onError
                        withContext(Dispatchers.Main) {
                            onError("Login failed: Invalid response")
                        }
                    }
                } else {
                    // Switch to the main thread to call onError
                    withContext(Dispatchers.Main) {
                        onError("Login failed: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Switch to the main thread to call onError
                withContext(Dispatchers.Main) {
                    onError("Error occurred: ${e.localizedMessage}")
                }
            }
        }
    }

    fun validateLogin():Boolean {

        if (email.value.isEmpty()) {
            snackbarMessage.value = "Email cannot be empty"
            return false
        }
        if (password.value.isEmpty()) {
            snackbarMessage.value = "Password cannot be empty"
            return false
        }

        return true
    }
}