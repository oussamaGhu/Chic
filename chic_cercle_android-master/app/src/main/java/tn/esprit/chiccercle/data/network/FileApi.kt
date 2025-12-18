package tn.esprit.chiccercle.data.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FileApi {

    @Multipart
    @POST("file/upload/{type}")
    fun uploadFile(
        @Path("type") type: String,
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>

    @GET("file/{id}")
    @Streaming
    fun getFile(
        @Path("id") id: String
    ): Call<ResponseBody>
}

// Data classes for responses
data class UploadResponse(
    val fileId: String
)

data class FileRecord(
    val id: String,
    val name: String,
    val path: String,
    val mimetype: String,
    val size: Long
)