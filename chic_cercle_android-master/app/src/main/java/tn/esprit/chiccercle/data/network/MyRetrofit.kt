package tn.esprit.chiccercle.data.network;

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
object MyRetrofit {
    private const val BASE_URL = "http://192.168.43.95:3000/"
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Délai d'attente pour établir la connexion
        .writeTimeout(30, TimeUnit.SECONDS)   // Délai d'attente pour l'écriture
        .readTimeout(30, TimeUnit.SECONDS)    // Délai d'attente pour la lecture
        .build()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    fun getBaseUrl(): String {
        return BASE_URL
    }
    val userAPI: UserAPI by lazy {
        retrofit.create(UserAPI::class.java)
    }

    val fileApi: FileApi by lazy {
        retrofit.create(FileApi::class.java)
    }

    val marketAPI: MarketAPI by lazy {
        retrofit.create(MarketAPI::class.java)
    }
    val clothesApi: ClothesApi by lazy {
        retrofit.create(ClothesApi::class.java)
    }
    val getReadyApi: GetReadyApi by lazy {
        retrofit.create(GetReadyApi::class.java)
    }
    val assembleApi: AssembleApi by lazy {
        retrofit.create(AssembleApi::class.java)
    }

    val requestApi: RequestApi by lazy {
        retrofit.create(RequestApi::class.java)
    }

}














