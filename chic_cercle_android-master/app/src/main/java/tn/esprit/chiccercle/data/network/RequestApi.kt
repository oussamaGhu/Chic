package tn.esprit.chiccercle.data.network


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import tn.esprit.chiccercle.model.Request // Ensure you have the Request model imported

interface RequestApi {

    // POST request to create a new request
    @POST("request")
    suspend fun createRequest(@Body createRequestDto: Request): Response<Request>

    // GET request to fetch all requests
    @GET("request")
    suspend fun getAllRequests(): Response<List<Request>>

    // GET request to fetch a single request by ID
    @GET("request/{id}")
    suspend fun getRequestById(@Path("id") id: String): Response<Request>

    // PATCH request to update a request by ID
    @PATCH("request/{id}")
    suspend fun updateRequest(@Path("id") id: String, @Body updateRequestDto: Request): Response<Request>

    // DELETE request to remove a request by ID
    @DELETE("request/{id}")
    suspend fun deleteRequest(@Path("id") id: String): Response<Unit>
}