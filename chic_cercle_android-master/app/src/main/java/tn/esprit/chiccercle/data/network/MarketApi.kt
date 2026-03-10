package tn.esprit.chiccercle.data.network
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import tn.esprit.chiccercle.model.Assemble

interface MarketAPI {
    // POST request to create an assemble
    /*@POST("assemble")
    suspend fun createAssemble(@Body assemble: Assemble): Response<Assemble>*/

    // GET request to fetch all assembles
    @GET("assemble")
    suspend fun getAllAssembles(): Response<List<Assemble>>

    // GET request to fetch a single assemble by ID
    @GET("assemble/{id}")
    suspend fun getAssembleById(@Path("id") id: String): Response<Assemble>

    // PATCH request to update an assemble
    @PATCH("assemble/{id}")
    suspend fun updateAssemble(@Path("id") id: String, @Body assemble: Assemble): Response<Assemble>

    // DELETE request to remove an assemble by ID
    @DELETE("assemble/{id}")
    suspend fun deleteAssemble(@Path("id") id: String): Response<Unit>
}