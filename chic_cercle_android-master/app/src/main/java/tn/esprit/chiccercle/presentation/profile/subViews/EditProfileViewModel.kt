package tn.esprit.chiccercle.presentation.profile.subViews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.model.User

class EditProfileVIewModel : ViewModel()  {
    // Load user data by ID
    fun loadUserData(userId: String, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                println("Fetching user with ID: $userId")
                val response = MyRetrofit.userAPI.getUserById(userId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        println("User data fetched: $it")
                        onSuccess(it)
                    } ?: onError("User not found")
                } else {
                    onError("Error fetching user: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Exception occurred: ${e.message}")
            }
        }
    }


    // Update user data
    fun updateUserData(userId: String, updatedUser: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Call the API with userId and updated user details
                val response = MyRetrofit.userAPI.updateUser(userId, updatedUser)
                if (response.isSuccessful) {
                    println("API Response: ${response.body()}")
                    onSuccess()
                } else {
                    println("Error Response: ${response.errorBody()?.string()}")
                    onError("Error updating user: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
                onError("Exception occurred: ${e.message}")
            }
        }
    }


}