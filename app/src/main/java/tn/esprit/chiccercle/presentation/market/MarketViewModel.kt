package tn.esprit.chiccercle.presentation.market

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.model.Clothes
import android.util.Log
import android.widget.Toast
import tn.esprit.chiccercle.model.Assemble
import tn.esprit.chiccercle.model.Request


public class MarketViewModel(

) : ViewModel() {
    private val _assembles = mutableStateOf<List<Assemble>>(emptyList())
    val assembles: State<List<Assemble>> = _assembles

    private val _clothes = mutableStateOf<List<Clothes>>(emptyList())
    val clothes: State<List<Clothes>> = _clothes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _userRole = MutableStateFlow<String>("")
    val userRole: StateFlow<String> get() = _userRole

    // Set the user role after login
    fun setUserRole(role: String) {
        _userRole.value = role
    }

    // Fetch assembles from the backend
    fun fetchClothes() {
        viewModelScope.launch {
            try {
                Log.d("MarketViewModel", "Fetching clothes from the backend...") // Log the start of the fetch process

                val response = MyRetrofit.clothesApi.getAllClothes()

                if (response.isSuccessful) {
                    Log.d("MarketViewModel", "Fetch successful! Response: ${response.body()}") // Log the successful response
                    // Filter the clothes to include only those with a price not equal to 0
                    val filteredClothes = response.body()?.filter { it.price != 0.0 } ?: emptyList()
                    _clothes.value = filteredClothes
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MarketViewModel", "Error fetching clothes: $errorBody") // Log the error response
                    _error.value = "Error fetching clothes: $errorBody"
                }
            } catch (e: Exception) {
                Log.e("MarketViewModel", "Network error: ${e.message}", e) // Log the exception
                _error.value = "Network error: ${e.message}"
            }
        }
    }
    // ViewModel for fetching assembles
// ViewModel for fetching assembles
    fun fetchAssembles() {
        viewModelScope.launch {
            try {
                Log.d("MarketViewModel", "Fetching assembles from the backend...")

                val response = MyRetrofit.marketAPI.getAllAssembles()

                if (response.isSuccessful) {
                    Log.d("MarketViewModel", "Fetch successful! Response: ${response.body()}")
                    // Filter assembles to include only those with price != 0
                    val filteredAssembles = response.body()?.filter { it.price != 0.0 } ?: emptyList()
                    _assembles.value = filteredAssembles
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MarketViewModel", "Error fetching assembles: $errorBody")
                    _error.value = "Error fetching assembles: $errorBody"
                }
            } catch (e: Exception) {
                Log.e("MarketViewModel", "Network error: ${e.message}", e)
                _error.value = "Network error: ${e.message}"
            }
        }
    }


    fun addRequest(context: Context, request: Request) {
        viewModelScope.launch {
            try {
                val response = MyRetrofit.requestApi.createRequest(request)
                if (response.isSuccessful) {
                    Log.d("MarketViewModel", "Request added successfully")
                    // Show toast for successful request
                    Toast.makeText(context, "Request sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("MarketViewModel", "Failed to add request: ${response.errorBody()?.string()}")
                    // Show toast for failed request
                    Toast.makeText(context, "Request Already Sent", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MarketViewModel", "Error adding request: ${e.message}", e)
                // Show toast for exception (e.g., network error)
                Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}