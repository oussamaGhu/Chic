package tn.esprit.chiccercle.presentation.market.seller
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.data.DataInitializer
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.model.Clothes
import tn.esprit.chiccercle.model.Request


class SellerMarkerViewModel : ViewModel(){
    // Use mutableStateOf to hold the list of requests and UI states
    var requests = mutableStateOf<List<Request>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    init {
        fetchRequests()
    }

    // Fetch requests from DataInitializer
     fun fetchRequests() {
        viewModelScope.launch {
            // Set loading state to true when fetching
            isLoading.value = true
            errorMessage.value = null

            try {
                // Fetch requests using DataInitializer
                DataInitializer.getAllRequests() // This should update DataInitializer's LiveData
                // Update the state with the fetched requests from DataInitializer
                val allRequests = DataInitializer.getRequestData()


                val filteredRequests = allRequests.filter {
                    it.sellerId == SessionManager().getUserId() && it.isSold == false
                }

                // Update the requests state with the filtered list
                requests.value = filteredRequests


            } catch (e: Exception) {
                // Handle error and update error message
                errorMessage.value = "Failed to fetch requests: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun deleteRequest(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Call the API to delete the request
                val response = MyRetrofit.requestApi.deleteRequest(id)

                if (response.isSuccessful) {
                    // If deletion is successful, call onSuccess
                    onSuccess()
                    fetchRequests()
                } else {
                    // If deletion fails, pass the error message to onError
                    onError("Failed to delete request: ${response.message()}")
                }
            } catch (e: Exception) {
                // If an exception occurs, pass the exception message to onError
                onError("Error: ${e.message}")
            }
        }
    }

    fun updateRequest(id: String, updatedRequest: Request, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = MyRetrofit.requestApi.updateRequest(id, updatedRequest)
                if (response.isSuccessful) {
                    onSuccess()
                    fetchRequests()
                } else {
                    onError("Failed to update request: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }


    fun updateClothes(clothingId: String, updatedClothes: Clothes) {
        viewModelScope.launch {
            try {
                val response = MyRetrofit.clothesApi.updateClothes(clothingId, updatedClothes)
                if (response.isSuccessful) {
print("yes")

                } else {
                    print("no")
                }
            } catch (e: Exception) {
                print("no")
            }
        }
    }
}