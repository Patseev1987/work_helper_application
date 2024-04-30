package ru.bogdan.patseev_diploma.data

import kotlinx.coroutines.flow.Flow
import ru.bogdan.patseev_diploma.data.web.ApiHelper
import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import javax.inject.Inject

class ApplicationRepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper
) : ApplicationRepository {
    override suspend fun loadStorageRecordByWorkerId(
        workerId: Long,
        toolType: ToolType,
        toolCode: String
    ): List<StorageRecord> {
        return apiHelper.loadStorageRecordByWorkerId(workerId, toolType, toolCode)
    }

    override fun checkLogin(login: String, password: String): Flow<Worker> {
        return apiHelper.checkLogin(login, password)
    }

    override suspend fun createTransaction(
        sender: Worker,
        receiver: Worker,
        tool: Tool,
        amount: Int
    ): Transaction {
        return apiHelper.createTransaction(sender, receiver, tool, amount)
    }

    override fun loadTransactionsByWorkerId(workerId: Long): Flow<List<Transaction>> {
        return apiHelper.loadTransactionsByWorkerId(workerId)
    }

    override suspend fun loadWorkersByDepartment(department: Department): List<Worker> {
        return apiHelper.loadWorkersByDepartment(department)
    }

    override suspend fun loadStorageWorkerByDepartment(department: Department): Worker {
        return apiHelper.loadStorageWorkerByDepartment(department)
    }

    override suspend fun loadAmountByWorkerAndTool(worker: Worker, tool: Tool): Int {
        return apiHelper.loadAmountByWorkerAndTool(worker, tool)
    }

    override suspend fun loadToolsForSearch(code: String): List<Tool> {
        return apiHelper.loadToolsForSearch(code)
    }

    override suspend fun loadTransactionsWithAnotherDepartment(
        anotherDepartment: Department,
        page: Int,
        toolCode: String
    ): List<Transaction> {
        return apiHelper.loadTransactionsWithAnotherDepartment(anotherDepartment, page, toolCode)
    }


}