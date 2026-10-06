package tn.esprit.chiccercle.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import tn.esprit.chiccercle.model.User
import tn.esprit.chiccercle.presentation.auth.signUp.SignUpViewModel

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val accessToken: String,val refreshToken: String, val userId: String, val role: String)

interface UserAPI {

    @POST("auth/signUp")
    suspend fun signup(@Body user: User): Response<LoginResponse>

    @PATCH("user/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,        // User ID as a path parameter
        @Body updateUserDto: User         // Updated user data
    ): Response<User>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("user/{id}")
    suspend fun getUserById(@Path("id") userId: String): Response<User>


    // Send password reset link
    @POST("/auth/forgot-password")
    suspend fun sendPasswordResetLink(@Body email: String): Response<Void>


    @POST("auth/register") // Replace with your actual endpoint
    suspend fun registerUser(@Body user: User): Response<User>
}


