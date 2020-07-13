package com.islery.catstestapp.network

import com.islery.catstestapp.data.model.Cat
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val BASE_URL = "https://api.thecatapi.com/v1/"
private const val API_KEY = "81e9599f-8b32-4ca7-ba44-2e61a9db1081"


interface CatsApiService {
    @Headers("x-api-key: $API_KEY")
    @GET("images/search?order=ASC&size=med")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<Cat>
}



