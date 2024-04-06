package ru.bogdan.m17_recyclerview.data


import retrofit2.http.GET
import retrofit2.http.Query
import ru.bogdan.m17_recyclerview.data.pojo.PhotosFromNASAApi
import ru.bogdan.m17_recyclerview.util.API_KEY

interface ApiService {
    @GET("photos")
    suspend fun loadPhotos(
        @Query("earth_date") earthDate: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): PhotosFromNASAApi

}

