package tn.esprit.chiccercle.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tn.esprit.chiccercle.model.AssembleRequest
import tn.esprit.chiccercle.model.Clothes
interface GetReadyApi {
    @POST("gemini/generate")
    suspend fun generateAssemble(@Body  assembleRequest: AssembleRequest): Response<List<Clothes>>
}
