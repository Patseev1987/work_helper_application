package ru.bogdan.m17_recyclerview.data




import retrofit2.http.GET
import retrofit2.http.Query
import ru.bogdan.patseev_diploma.data.web.pojo.WorkerWEB


interface ApiService {
    @GET("tools")
   suspend fun loadTools()




    @GET("check_login")
    suspend fun checkLogin(
        @Query("login") login: String,
        @Query("password") password: String
    ): WorkerWEB
}

