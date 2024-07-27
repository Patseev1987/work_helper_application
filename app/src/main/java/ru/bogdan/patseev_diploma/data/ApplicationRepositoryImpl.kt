package ru.bogdan.patseev_diploma.data

import kotlinx.coroutines.flow.Flow
import ru.bogdan.patseev_diploma.data.web.ApiHelper
import ru.bogdan.patseev_diploma.data.web.pojo.Token
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
        token:String,
        workerId: Long,
        toolType: ToolType,
        toolCode: String
    ): List<StorageRecord> {
        return apiHelper.loadStorageRecordByWorkerId(token, workerId, toolType, toolCode)
    }

    override suspend fun checkLogin(login: String, password: String): Token {
        return apiHelper.checkLogin(login, password)
    }

    override suspend fun createTransaction(
        token:String,
        sender: Worker,
        receiver: Worker,
        tool: Tool,
        amount: Int
    ): Transaction {
        return apiHelper.createTransaction(token, sender, receiver, tool, amount)
    }

    override fun loadTransactionsByWorkerId( token:String,workerId: Long): Flow<List<Transaction>> {
        return apiHelper.loadTransactionsByWorkerId(token, workerId)
    }

    override suspend fun loadWorkersByDepartment( token:String,department: Department): List<Worker> {
        return apiHelper.loadWorkersByDepartment(token, department)
    }

    override suspend fun loadStorageWorkerByDepartment( token:String,department: Department): Worker {
        return apiHelper.loadStorageWorkerByDepartment(token, department)
    }

    override suspend fun loadAmountByWorkerAndTool( token:String, worker: Worker, tool: Tool): Int {
        return apiHelper.loadAmountByWorkerAndTool( token, worker, tool)
    }

    override suspend fun loadToolsForSearch( token:String, code: String): List<Tool> {
        return apiHelper.loadToolsForSearch(token, code)
    }

    override suspend fun loadTransactionsWithAnotherDepartment(
        token:String,
        anotherDepartment: Department,
        toolCode: String
    ): List<Transaction> {
        return apiHelper.loadTransactionsWithAnotherDepartment(token ,anotherDepartment, toolCode)
    }

    override suspend fun updateTransactions() {
        apiHelper.updateTransactions()
    }

    override suspend fun loadWorkerById(token: String, id: Long): Worker {
      return  apiHelper.loadWorkerById(token, id)
    }
}