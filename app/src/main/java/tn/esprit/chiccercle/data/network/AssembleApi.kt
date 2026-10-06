package tn.esprit.chiccercle.data.network
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tn.esprit.chiccercle.model.Assemble

interface AssembleApi {
    @POST("assemble")
    suspend fun createAssemble(@Body assemble: Assemble): Response<Assemble>

    @GET("assemble/getByUser/{userId}")
    suspend fun getAssembleByuserId(@Path("userId") userId: String): List<Assemble>
    @DELETE("assemble/{assembleId}")
    suspend fun deleteAssemble(@Path("assembleId") assembleId: String): Response<Void>


}