package ru.bogdan.patseev_diploma.data.web

import retrofit2.Response
import retrofit2.http.*
import ru.bogdan.patseev_diploma.data.web.pojo.*
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType


interface ApiService {
    @GET("api/records/workerId")
    suspend fun loadStorageRecordsByWorkerId(
        @Header("Authorization") token: String,
        @Query("workerId") workerId: Long,
        @Query("toolType") toolType: ToolType,
        @Query("toolCode") toolCode: String
    ): List<StorageRecordWEB>

    @GET("api/transactions/worker")
    suspend fun loadTransactionsByWorkerId(
        @Header("Authorization") token: String,
        @Query("workerId") workerId: Long,
    ): List<TransactionWEB>


    @POST("auth/sign-in")
    suspend fun checkLogin(
    @Body userDTOSignIn: UserDTOSignIn
    ):Response<Token>

    @GET("api/storage_worker_by_department")
    suspend fun loadStorageWorkerByDepartment(
        @Header("Authorization") token: String,
        @Query("department") department: Department
    ): WorkerWEB

    @GET("api/workers_by_department")
    suspend fun loadWorkersByDepartment(
        @Header("Authorization") token: String,
        @Query("department") department: Department
    ): List<WorkerWEB>


    @POST("api/transaction/create")
    suspend fun createTransaction(
        @Header("Authorization") token: String,
        @Body transaction: TransactionWEB): TransactionWEB

    @GET("api/tools/code")
    suspend fun loadToolsForSearch(
        @Header("Authorization") token: String,
        @Query("code") code: String): List<ToolWEB>

    @GET("api/records/amount")
    suspend fun loadAmountByWorkerAndTool(
        @Header("Authorization") token: String,
        @Query("workerId") workerId: Long,
        @Query("toolCode") toolCode: String
    ): Int

    @GET("api/transactions/actionWithAnotherDepartments")
    suspend fun loadTransactionsWithAnotherDepartment(
        @Header("Authorization") token: String,
        @Query("anotherDepartment") anotherDepartment: Department,
        @Query("toolCode") toolCode: String
    ): List<TransactionWEB>


    @GET("api/worker_by_id/{id}")
    suspend fun loadWorkerById(
        @Header("Authorization") token: String,
        @Path("id") workerId: Long
    ): WorkerWEB


    @GET("api/records/{toolCode}")
    suspend fun getRecordsByToolCode(
        @Header("Authorization") token: String,
        @Path("toolCode") toolCode:String
    ):List<StorageRecordWEB>
}

