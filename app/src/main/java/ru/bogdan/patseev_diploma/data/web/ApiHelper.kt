package ru.bogdan.patseev_diploma.data.web


import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.bogdan.patseev_diploma.data.web.pojo.StorageRecordWEB
import ru.bogdan.patseev_diploma.data.web.pojo.ToolWEB
import ru.bogdan.patseev_diploma.data.web.pojo.TransactionWEB
import ru.bogdan.patseev_diploma.data.web.pojo.WorkerWEB
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType


interface ApiHelper {

    suspend fun loadStorageRecordByWorkerId(
        workerId: Long,
        toolType: ToolType,
        toolCode: String = ""
    ): List<StorageRecord>

    suspend fun checkLogin(login: String, password: String): Worker
    suspend fun createTransaction(
        sender: Worker,
        receiver: Worker,
        tool: Tool,
        amount: Int
    ): Transaction

    fun loadTransactionsByWorkerId(workerId: Long): Flow<List<Transaction>>

    suspend fun loadWorkersByDepartment(department: Department): List<Worker>

    suspend fun loadStorageWorkerByDepartment(department: Department): Worker

    suspend fun loadAmountByWorkerAndTool(worker: Worker, tool: Tool): Int

    suspend fun loadToolsForSearch(code: String): List<Tool>

    suspend fun loadTransactionsWithAnotherDepartment(
        anotherDepartment: Department,
        toolCode: String = ""
    ): List<Transaction>

    suspend fun updateTransactions()
}