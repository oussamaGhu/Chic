package tn.esprit.chiccercle.presentation.getReady

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import org.bson.types.ObjectId
import tn.esprit.chiccercle.data.DataInitializer

import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.model.Assemble
import tn.esprit.chiccercle.model.AssembleRequest
import tn.esprit.chiccercle.model.Clothes

import java.util.Date
import kotlinx.coroutines.delay


class GetReadyViewModel : ViewModel() {
    private val _AssembleList = MutableLiveData<MutableList<Assemble>>(mutableListOf())
    val AssembleList: LiveData<MutableList<Assemble>> get() = _AssembleList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage// Ajouter cette ligne
    val newId = ObjectId().toString()
    var clothesFilter: Clothes = Clothes(
        images = null,
        user = SessionManager().getUserId().toString(),
        occasions = mutableListOf(),
        moods = mutableListOf(),
        weather = mutableListOf(),
        colors = mutableListOf(),
        id = newId,
        price = 0.0
    )
    private val _generatedList = MutableLiveData<List<Clothes>>()
    val generatedList: LiveData<List<Clothes>> get() = _generatedList

    fun assembleGemini(request: AssembleRequest, callback: (Boolean, String?) -> Unit) {
        _isLoading .value = true

        _generatedList.postValue(emptyList())
        viewModelScope.launch {
            delay(6000)
            try {
                val response =
                    MyRetrofit.getReadyApi.generateAssemble(request) // Assurez-vous d'avoir votre service API configuré
                if (response.isSuccessful) {
                    response.body()?.let { clothesList ->
                        _generatedList.postValue(clothesList)// Mettez à jour la liste générée
                        callback(true, null)
                    } ?: callback(false, "Réponse vide de l'API")
                } else {
                    _generatedList.postValue(emptyList())
                    callback(false, "Erreur : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                callback(false, "Erreur de connexion : ${e.message}")
            } finally {
                // Indiquez que le chargement est terminé
                _isLoading .value = false

            }
        }
    }

    fun addAssemble(
        name: String,
        images: String,
        date: Date,
        price: Double,
        clothesIds: List<String>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("Request", "Appel de addAssemble")
        val userId = SessionManager().getUserId().toString()
        Log.d("id", "User ID récupéré : $userId")
        val newId = ObjectId()
        viewModelScope.launch {
            try {
                // Créez un objet AssembleRequest à envoyer au backend
                val assembleRequest = Assemble(
                    id = newId.toString(),
                    name = name,
                    clothes = clothesIds,
                    user = userId,
                    image = images,
                    date = date,
                    price = price
                )
                Log.d("Request", "Request: $assembleRequest")
                // Appelez le backend via le repository
                val response = MyRetrofit.assembleApi.createAssemble(assembleRequest)

                if (response.isSuccessful && response.body() != null) {
                    val updatedAsssemble = DataInitializer.getAssemble(userId)
                    _AssembleList.value = updatedAsssemble.toMutableList()
                    onSuccess() // Succès, appelez la fonction de rappel
                } else {
                    // onError("Erreur: ${response.errorBody()?.string() ?: "Erreur inconnue"}")
                    Log.d("Response", "Erreur dans la réponse: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onError("Exception: ${e.message ?: "Erreur inconnue"}")
            }
        }
    }

    fun getAssemble(userId: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // Fetch assembles from the API
                val response = MyRetrofit.marketAPI.getAllAssembles()
                if (response.isSuccessful) {
                    Log.d("MarketViewModel", "Fetch successful! Response: ${response.body()}")

                    // Filter the assembles based on the provided userId
                    val filteredAssembles = response.body()?.filter { assemble ->
                        assemble.user == userId
                    } ?: emptyList()

                    // Update the AssembleList with the filtered results
                    _AssembleList.value = filteredAssembles as MutableList<Assemble>?
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MarketViewModel", "Error fetching assembles: $errorBody")
                    _errorMessage.value = "Error fetching assembles: $errorBody"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load Assemble: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAssemble(assembleId: String) {
        viewModelScope.launch {
            try {
                val response = MyRetrofit.assembleApi.deleteAssemble(assembleId)
                if (response.isSuccessful) {
                    Log.d("DeleteClothes", "Clothing item deleted successfully")
                    _AssembleList.value = _AssembleList.value?.filter { it.id != assembleId }?.toMutableList()
                    _errorMessage.value = null
                } else {
                    Log.e("DeleteAssemble", "Failed to delete item: ${response.code()}")
                    _errorMessage.value = "Failed to delete item: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("DeleteClothes", "Error: ${e.message}")
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}





