package tn.esprit.chiccercle.presentation.auth.signUp

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.model.User
import java.io.File
import java.util.Date

import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.chiccercle.data.network.MyRetrofit.fileApi
import tn.esprit.chiccercle.data.network.UploadResponse
import java.io.FileOutputStream


class SignUpViewModel : ViewModel() {
    val name = mutableStateOf("")
    val phoneNumber = mutableStateOf<Int?>(null)
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val gender = mutableStateOf("Select Gender")
    val userType = mutableStateOf("Select Type")
    val dateOfBirth = mutableStateOf( Date())
    val address = mutableStateOf("")
    val selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedMorphology = mutableStateOf("Select your morphology")
    val height = mutableFloatStateOf(17f)
    val weight = mutableFloatStateOf(17f)
    val snackbarMessage = mutableStateOf("")
    val fileId = mutableStateOf("")

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    suspend fun navigateTo(target: String) {
        _navigationEvent.emit(target)
    }
    private val sessionManager = SessionManager()


    fun saveImageUriToFile(context: Context, uri: Uri?, fileName: String): File? {
        uri ?: return null
        val resolver: ContentResolver = context.contentResolver
        val bitmap = ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(resolver, uri)
        )
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
        val file = File(directory, "$fileName.png")

        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }

        return file
    }




    fun uploadFile( file: File,  onResult: (String) -> Unit) {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        fileApi.uploadFile("user",body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body()?.fileId ?: "Upload successful!")
                    fileId.value = response.body()?.fileId ?: ""

                } else {
                    onResult("Upload failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                onResult("Upload failed: ${t.message}")
            }
        })
    }


    fun downloadFile(context: Context, fileId: String, onResult: (String) -> Unit) {
        fileApi.getFile(fileId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
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


    fun signUp(fileId: String,onSuccess: () -> Unit, onError:  (String) -> Unit) {
        val user = User(
            name = name.value,
            email = email.value,
            password = password.value,
            phoneNumber = phoneNumber.value,
            pictureProfile = fileId,
            role = userType.value,
            birthday = dateOfBirth.value,
            clothes = emptyList(),
            assemble = emptyList(),
            height = height.floatValue.toInt(),
            weight = weight.floatValue.toInt(),
            shape = selectedMorphology.value,
            location = null)



        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = MyRetrofit.userAPI.signup(user)
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


    // Validation methods
    fun validateStep1(): Boolean {
        var isValid = true

        if (email.value.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            snackbarMessage.value = "Invalid email address"
            isValid = false
        }

        if (password.value != confirmPassword.value || password.value.isBlank()) {
            snackbarMessage.value = "Passwords do not match or are empty"
            isValid = false
        }

        val strongPasswordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
         if ( strongPasswordPattern.matches(password.value)){
             snackbarMessage.value = "Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, a number, and a special character"
             isValid = false
         }

        return isValid
    }

    fun validateStep2(): Boolean {
        var isValid = true
        if (gender.value == "Select Gender") {
            snackbarMessage.value = "Please select a gender"
            isValid = false
        }
        if (userType.value == "Select Type") {
            snackbarMessage.value = "Please select a user type"
            isValid = false
        }
        if (dateOfBirth.value.toString().isBlank()) {
            snackbarMessage.value = "Please select a date of birth"
            isValid = false
        }
        if (address.value.isBlank()) {
            snackbarMessage.value = "Please enter an address"
            isValid = false
        }

        return isValid
    }

    fun validateStep3(): Boolean {
        var isValid = true
        if (selectedMorphology.value == "Select your morphology") {
            snackbarMessage.value = "Please select your morphology"
            isValid = false
        }
        if (height.floatValue.toInt() < 10) {
            snackbarMessage.value = "Please enter a valid height"
            isValid = false
        }

        if (weight.floatValue.toInt() < 10) {
            snackbarMessage.value = "Please enter a valid weight"
            isValid = false
        }
        return isValid

    }



}