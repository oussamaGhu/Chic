package tn.esprit.chiccercle.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import tn.esprit.chiccercle.model.Clothes


data class ClothesResponse(
    @SerializedName("_id")  // This maps the backend "_id" to "id" in your model
    val id: String,
    val images: String, // Correspond à l'ID MongoDB sous forme de chaîne
    val occasions: List<String>,
    val moods: List<String>,
    val weather: List<String>,
    val types: List<String >,
    val colors: List<String>
)

interface ClothesApi {
    @GET("clothes/getByUser/{userId}")
    suspend fun getClothesByUserId(@Path("userId") userId: String): List<Clothes>

    @DELETE("clothes/{clothingId}")
    suspend fun deleteClothes(@Path("clothingId") clothingId: String): Response<Void>

    @PATCH("clothes/{clothingId}")
    suspend fun updateClothes(@Path("clothingId") clothingId: String, @Body updatedClothes: Clothes): Response<Clothes>
    @POST("clothes/addclothes")
    suspend fun addClothes(@Body newClothes: Clothes): Response<ClothesResponse>
    // Fetch all clothes
    @GET("clothes")
    suspend fun getAllClothes(): Response<List<Clothes>>
}
