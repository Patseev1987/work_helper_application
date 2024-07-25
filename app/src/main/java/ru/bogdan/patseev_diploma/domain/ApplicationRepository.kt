package ru.bogdan.patseev_diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.bogdan.patseev_diploma.data.web.pojo.Token
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType

interface ApplicationRepository {
    suspend fun loadStorageRecordByWorkerId(
        workerId: Long,
        toolType: ToolType,
        toolCode: String = ""
    ): List<StorageRecord>

    suspend fun checkLogin(login: String, password: String): Token
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