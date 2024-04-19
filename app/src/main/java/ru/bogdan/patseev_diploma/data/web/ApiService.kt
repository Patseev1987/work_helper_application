package ru.bogdan.m17_recyclerview.data




import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.bogdan.patseev_diploma.data.web.pojo.WorkerWEB
import ru.bogdan.patseev_diploma.domain.models.StorageRecord


interface ApiService {
    @GET("tools/{worker_id}")
   suspend fun loadToolsByWorkerId(@Path("worker_id") workerId:Long ):List<StorageRecord>




    @GET("check_login")
    suspend fun checkLogin(
        @Query("login") login: String,
        @Query("password") password: String
    ): WorkerWEB
}

