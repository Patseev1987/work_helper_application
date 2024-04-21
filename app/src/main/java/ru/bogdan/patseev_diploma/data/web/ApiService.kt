package ru.bogdan.m17_recyclerview.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.bogdan.patseev_diploma.data.web.pojo.StorageRecordWEB
import ru.bogdan.patseev_diploma.data.web.pojo.TransactionWEB
import ru.bogdan.patseev_diploma.data.web.pojo.WorkerWEB
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.enums.Department


interface ApiService {
    @GET("records/{workerId}")
   suspend fun loadStorageRecordsByWorkerId(@Path("workerId") workerId:Long ):List<StorageRecordWEB>

   @GET("transaction/{workerId}")
   suspend fun loadTransactionsByWorkerId(@Path("workerId") workerId:Long ):List<TransactionWEB>


    @GET("check_login")
    suspend fun checkLogin(
        @Query("login") login: String,
        @Query("password") password: String
    ): WorkerWEB

    @GET("storage_worker_by_department")
    suspend fun loadStorageWorkerByDepartment(
        @Query("department") department: Department
    ):WorkerWEB
}

