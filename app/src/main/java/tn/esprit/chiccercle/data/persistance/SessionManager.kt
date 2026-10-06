package tn.esprit.chiccercle.data.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.data.DataInitializer
import tn.esprit.chiccercle.model.User

class SessionManager {

    companion object {
        private const val PREF_NAME = "user_session"
        private const val KEY_USER_ID = "USER_ID"
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KEY_ROLE = "ROLE"
        private const val KEY_USER_NAME = "USER_NAME"
        private const val KEY_USER_EMAIL = "USER_EMAIL"
        private const val KEY_USER_PROFILE_PICTURE = "USER_PROFILE_PICTURE"

        private var sharedPreferences: SharedPreferences? = null
        private val editor: SharedPreferences.Editor
            get() = sharedPreferences?.edit() ?: throw IllegalStateException("SharedPreferences is not initialized.")

        fun initialize(context: Context) {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun saveUserId(userId: String) {
        editor.putString(KEY_USER_ID, userId).apply()
        GlobalScope.launch {
            // Calling the suspend function in a coroutine
            DataInitializer.initializeData(userId)
        }
    }

    fun getUserId(): String? {
        return sharedPreferences?.getString(KEY_USER_ID, null)
    }

    fun saveAccessToken(token: String) {
        editor.putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences?.getString(KEY_ACCESS_TOKEN, null)
    }

    fun saveRole(role: String) {
        editor.putString(KEY_ROLE, role).apply()
    }

    fun getRole(): String? {
        return sharedPreferences?.getString(KEY_ROLE, null)
    }

    fun clearSession() {
        editor.clear().apply()
    }
    fun getUser(): User {
        val name = sharedPreferences?.getString(KEY_USER_NAME, "") ?: ""
        val email = sharedPreferences?.getString(KEY_USER_EMAIL, "") ?: ""
        val pictureProfile = sharedPreferences?.getString(KEY_USER_PROFILE_PICTURE, "") ?: ""

        return User(
            name = name,
            email = email,
            password = "",  // Assuming you are not storing password here, or implement password retrieval
            role = "", // You can store role as well if needed
            pictureProfile = pictureProfile,
            clothes = emptyList(),  // You can adjust this part as per your need
            assemble = emptyList(), // You can adjust this part as per your need
            height = null,
            weight = null,
            shape = null,
            location = null,
            phoneNumber = null,

        )
    }
}