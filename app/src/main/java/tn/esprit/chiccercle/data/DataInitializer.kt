package tn.esprit.chiccercle.data
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response // Use the correct Response class from Retrofit

import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.model.Assemble
import tn.esprit.chiccercle.model.Clothes
import tn.esprit.chiccercle.model.Request
import tn.esprit.chiccercle.model.User

class DataInitializer {
    companion object {
        private var userData: User = User(

            name = "",
            email = "",
            password = "",
            clothes = emptyList(),
            role = "",
            assemble = emptyList()
        )
        private var clothesData: List<Clothes> = emptyList()
        private var assembleData: List<Assemble> = emptyList()
        private var requestData: List<Request> = emptyList()

        val isLoading = MutableLiveData<Boolean>()
        val errorMessage = MutableLiveData<String?>()

        // Function to initialize data
        suspend fun initializeData(userId: String): Response<User>? {
            return try {
                // Attempt to log in and fetch the token
                val response = MyRetrofit.userAPI.getUserById(userId)

                if (response.isSuccessful) {
                    // Extract user data from the login response
                    val body = response.body()
                    if (body != null) {
                        // Assuming the LoginResponse contains user details
                        userData = body
                        // Optionally fetch clothes or other related data if required
                        clothesData = body.clothes as List<Clothes>
                        assembleData = body.assemble as List<Assemble>
                    }

                    if (userData.role == "seller") {

                        val responseRequest = MyRetrofit.requestApi.getAllRequests()
                        val bodyRequest = responseRequest.body()
                        if (bodyRequest != null) {
                            requestData = bodyRequest
                            print(requestData.size)
                        }

                    }


                }

                // Return the login response
                response
            } catch (e: Exception) {
                // Handle exceptions (e.g., network errors)
                e.printStackTrace()
                null
            }
        }

        // Setter pour mettre à jour clothesData
        fun setClothesData(clothes: List<Clothes>) {
            clothesData = clothes
        }
        fun assemleData(assemble: List<Assemble>) {
            assembleData = assemble
        }

        fun getClothesData(): List<Clothes> = clothesData
        fun getAssembleData(): List<Assemble> = assembleData

        // Getter for user data
        fun getUserData(): User = userData


        fun getRequestData(): List<Request> = requestData

        // Fetch all requests
        suspend fun getAllRequests() {
            isLoading.value = true
            errorMessage.value = null

            try {
                // Fetch requests from the API
                val response = MyRetrofit.requestApi.getAllRequests()
                if (response.isSuccessful) {
                    val requests = response.body()
                    if (requests != null && requests.isNotEmpty()) {
                        requestData = requests
                    } else {
                        errorMessage.value = "No requests found."
                    }
                } else {
                    errorMessage.value = "Failed to fetch requests. Status code: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Failed to load requests: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }

        suspend fun getClothes(userId: String): List<Clothes> {
            isLoading.value = true
            errorMessage.value = null

            return try {
                // Fetch clothes from the API
                val clothes = MyRetrofit.clothesApi.getClothesByUserId(userId)
                if (clothes.isNotEmpty()) {
                    clothesData = clothes
                } else {
                    errorMessage.value = "No clothes found for this user."
                }
                clothesData // Return the updated list of clothes
            } catch (e: Exception) {
                errorMessage.value = "Failed to load clothes: ${e.message}"
                emptyList() // Return an empty list in case of an error
            } finally {
                isLoading.value = false
            }
        }
        suspend fun getAssemble(userId: String): List<Assemble> {
            isLoading.value = true
            errorMessage.value = null

            return try {
                // Récupérer les assembles depuis l'API
                val assembles = MyRetrofit.assembleApi.getAssembleByuserId(userId)
                if (assembles.isNotEmpty()) {
                    assembleData = assembles // Mettez à jour votre liste d'assemblages
                } else {
                    errorMessage.value = "No Assemble found for this user."
                }
                assembleData // Retourner la liste mise à jour des assembles
            } catch (e: Exception) {
                errorMessage.value = "Failed to load Assemble: ${e.message}"
                emptyList() // Retourner une liste vide en cas d'erreur
            } finally {
                isLoading.value = false
            }
        }

    }
}