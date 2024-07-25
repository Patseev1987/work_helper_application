package ru.bogdan.patseev_diploma.data.web

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.bogdan.patseev_diploma.data.web.pojo.*
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType


interface ApiService {
    @GET("records/workerId")
    suspend fun loadStorageRecordsByWorkerId(
        @Query("workerId") workerId: Long,
        @Query("toolType") toolType: ToolType,
        @Query("toolCode") toolCode: String
    ): List<StorageRecordWEB>

    @GET("transactions/worker")
    suspend fun loadTransactionsByWorkerId(
        @Query("workerId") workerId: Long,
    ): List<TransactionWEB>


    @POST("auth/sign-in")
    suspend fun checkLogin(
    @Body userDTOSignIn: UserDTOSignIn
    ):Response<Token>

    @GET("storage_worker_by_department")
    suspend fun loadStorageWorkerByDepartment(
        @Query("department") department: Department
    ): WorkerWEB

    @GET("workers_by_department")
    suspend fun loadWorkersByDepartment(
        @Query("department") department: Department
    ): List<WorkerWEB>


    @POST("transaction/create")
    suspend fun createTransaction(@Body transaction: TransactionWEB): TransactionWEB

    @GET("tools/code")
    suspend fun loadToolsForSearch(@Query("code") code: String): List<ToolWEB>

    @GET("records/amount")
    suspend fun loadAmountByWorkerAndTool(
        @Query("workerId") workerId: Long,
        @Query("toolCode") toolCode: String
    ): Int

    @GET("transactions/actionWithAnotherDepartments")
    suspend fun loadTransactionsWithAnotherDepartment(
        @Query("anotherDepartment") anotherDepartment: Department,
        @Query("toolCode") toolCode: String
    ): List<TransactionWEB>
}

