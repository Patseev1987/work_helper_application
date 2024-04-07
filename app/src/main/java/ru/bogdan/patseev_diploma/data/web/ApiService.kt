package ru.bogdan.m17_recyclerview.data


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("photos")
    suspend fun loadPhotos(
        @Query("earth_date") earthDate: String,
        @Query("page") page: Int
    )

//    @GET("/tool/code") suspend fun getTool(
//                @Body tool:Tool
//            )

}

