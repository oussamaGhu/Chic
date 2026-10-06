package tn.esprit.chiccercle.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.data.DataInitializer
import tn.esprit.chiccercle.model.User

class ProfileViewModel: ViewModel() {
    private val emptyUser = User(name = "", email = "", password = "", clothes = emptyList(), role = "", assemble = emptyList())

    var user = mutableStateOf(emptyUser)


    fun loadUserData() {
        viewModelScope.launch {
            user.value = DataInitializer.getUserData()
        }
    }
    // Update user data
    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            // Update user data in the backend
            user.value = updatedUser // Reflect changes locally
        }
    }


}

