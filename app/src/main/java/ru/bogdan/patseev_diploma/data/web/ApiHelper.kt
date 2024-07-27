package ru.bogdan.patseev_diploma.data.web


import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.bogdan.patseev_diploma.data.web.ApiFactory.apiService
import ru.bogdan.patseev_diploma.data.web.pojo.*
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType


interface ApiHelper {

    suspend fun loadStorageRecordByWorkerId(
        token:String,
        workerId: Long,
        toolType: ToolType,
        toolCode: String = ""
    ): List<StorageRecord>

    suspend fun checkLogin(login: String, password: String): Token
    suspend fun createTransaction(
        token:String,
        sender: Worker,
        receiver: Worker,
        tool: Tool,
        amount: Int
    ): Transaction

    fun loadTransactionsByWorkerId( token:String,workerId: Long): Flow<List<Transaction>>

    suspend fun loadWorkersByDepartment( token:String,department: Department): List<Worker>

    suspend fun loadStorageWorkerByDepartment( token:String,department: Department): Worker

    suspend fun loadAmountByWorkerAndTool( token:String,worker: Worker, tool: Tool): Int

    suspend fun loadToolsForSearch( token:String,code: String): List<Tool>

    suspend fun loadTransactionsWithAnotherDepartment(
        token:String,
        anotherDepartment: Department,
        toolCode: String = ""
    ): List<Transaction>

    suspend fun updateTransactions()

    suspend fun loadWorkerById(token:String,id:Long): Worker
}