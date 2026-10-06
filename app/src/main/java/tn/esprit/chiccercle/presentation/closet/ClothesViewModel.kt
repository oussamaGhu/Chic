package tn.esprit.chiccercle.presentation.closet

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.model.Clothes
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.bson.types.ObjectId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.chiccercle.data.DataInitializer
import tn.esprit.chiccercle.data.network.MyRetrofit.clothesApi
import tn.esprit.chiccercle.data.network.MyRetrofit.fileApi
import tn.esprit.chiccercle.data.network.UploadResponse
import tn.esprit.chiccercle.data.persistence.SessionManager
import java.io.File
import java.io.FileOutputStream

// Fetch clothes for a specific user
    class ClothesViewModel : ViewModel() {
    private val _clothesList = MutableLiveData<List<Clothes>>()
    val clothesList: LiveData<List<Clothes>> get() = _clothesList
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage
    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog
    val price = mutableStateOf("") // Prix sous forme de chaîne
    val size = mutableStateOf("") // Taille sous forme de chaîne
    val description = mutableStateOf("") // Description sous forme de chaîne
    val clothesName = mutableStateOf("")


    // Fetch clothes for a specific user
    fun getClothes(userId: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val clothes = MyRetrofit.clothesApi.getClothesByUserId(userId)
                if (clothes.isNotEmpty()) {
                    _clothesList.value = clothes
                } else {
                    _errorMessage.value = "No clothes found for this user."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load clothes: ${e.message}"
            } finally {
                _isLoading.value = false
            }

        }
    }

    // Show/Hide the dialog
    fun toggleDialog() {
        _showDialog.value = !_showDialog.value
    }

    // Delete a specific clothing item by its ID
    fun deleteClothes(clothingId: String) {
        viewModelScope.launch {
            try {
                val response = clothesApi.deleteClothes(clothingId)
                if (response.isSuccessful) {
                    Log.d("DeleteClothes", "Clothing item deleted successfully")
                    _clothesList.value = _clothesList.value?.filter { it.id != clothingId }
                    _errorMessage.value = null
                } else {
                    Log.e("DeleteClothes", "Failed to delete item: ${response.code()}")
                    _errorMessage.value = "Failed to delete item: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("DeleteClothes", "Error: ${e.message}")
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateClothes(clothingId: String, updatedClothes: Clothes) {
        viewModelScope.launch {
            try {
                val response = MyRetrofit.clothesApi.updateClothes(clothingId, updatedClothes)
                if (response.isSuccessful) {

                    _clothesList.value = DataInitializer.getClothesData()

                    _clothesList.value = _clothesList.value!!.map {
                        if (it.id == clothingId) updatedClothes else it
                    }
                } else {
                    _errorMessage.value = "Failed to update clothing"
                    Log.d(
                        "UpdateClothes",
                        "Failed to update clothing: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error updating clothing: ${e.message}"
                Log.d("UpdateClothes", "Error: ${e.message}")
            }
        }
    }

    // 1. Convertir un Uri en fichier local
//  @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("NewApi")
    fun saveImageUriToFile(context: Context, uri: Uri?, fileName: String): File? {
        uri ?: return null
        val resolver: ContentResolver = context.contentResolver
        val bitmap = ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(resolver, uri)
        )
        val directory =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
        val file = File(directory, "$fileName.png")

        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return file
    }

    // 2. Uploader un fichier
    fun uploadFile(file: File, onResult: (String) -> Unit) {

        val fileId = mutableStateOf("")
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        fileApi.uploadFile("clothes",body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    val uploadedFileId = response.body()?.fileId ?: "Upload successful!"
                    fileId.value = uploadedFileId // Met à jour l'ID du fichier
                    onResult(uploadedFileId)
                } else {
                    onResult("Upload failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                onResult("Upload failed: ${t.message}")
            }
        })
    }

    // 3. Télécharger un fichier (optionnel)
    fun downloadFile(context: Context, fileId: String, onResult: (String) -> Unit) {
        fileApi.getFile(fileId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val file = File(context.cacheDir, "downloaded_file")
                    file.outputStream().use { outputStream ->
                        response.body()?.byteStream()?.copyTo(outputStream)
                    }
                    onResult("File downloaded: ${file.absolutePath}")
                } else {
                    onResult("Failed to download file: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResult("Failed to download file: ${t.message}")
            }
        })
    }

    fun addClothes(
        selectedImage: String,
        selectedOccasions: List<String>,
        selectedMoods: List<String>,
        selectedWeather: List<String>,
        selectedTypes: List<String>,
        selectedColors: List<String>,
        price: Double?,           // Ajoutez price
        name: String?,            // Ajoutez name (null si non fourni)
        description: String?,     // Ajoutez description (null si non fourni)
        size: String?

    ) {
        val userId = SessionManager().getUserId().toString()
        val newId = ObjectId()

        viewModelScope.launch {
            try {
                val newClothes = Clothes(
                    id = newId.toString(),
                    images = selectedImage,
                    occasions = selectedOccasions,
                    moods = selectedMoods,
                    weather = selectedWeather,
                    types = selectedTypes,
                    colors = selectedColors,
                    user = userId,
                    price = price ?: 0.0,  // Valeur par défaut si null
                    name = name ?: "",      // Valeur par défaut si null
                    description = description ?: "", // Valeur par défaut si null
                    size = size ?: ""
                )

                val response = MyRetrofit.clothesApi.addClothes(newClothes)

                if (response.isSuccessful && response.body() != null) {
                    val updatedClothes = DataInitializer.getClothes(userId)
                    _clothesList.value = updatedClothes
                } else {
                    _errorMessage.value = "Failed to add clothing"
                    Log.d("AddClothes", "Failed to add clothing: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error adding clothing: ${e.message}"
                Log.d("AddClothes", "Error: ${e.message}")
            }
        }
    }
}