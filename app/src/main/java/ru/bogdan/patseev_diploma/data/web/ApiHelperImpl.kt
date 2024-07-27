package ru.bogdan.patseev_diploma.data.web


import kotlinx.coroutines.flow.*
import ru.bogdan.patseev_diploma.data.web.mappers.toStorageRecord
import ru.bogdan.patseev_diploma.data.web.mappers.toTool
import ru.bogdan.patseev_diploma.data.web.mappers.toTransaction
import ru.bogdan.patseev_diploma.data.web.mappers.toTransactionWEB
import ru.bogdan.patseev_diploma.data.web.mappers.toWorker
import ru.bogdan.patseev_diploma.data.web.pojo.Token
import ru.bogdan.patseev_diploma.data.web.pojo.UserDTOSignIn
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import java.time.LocalDate
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService,
) : ApiHelper {

    private val updateTransactionsFlow: MutableSharedFlow<Unit> = MutableSharedFlow()

    override suspend fun loadStorageRecordByWorkerId(
        token:String,
        workerId: Long,
        toolType: ToolType,
        toolCode: String
    ): List<StorageRecord> {
        return apiService.loadStorageRecordsByWorkerId(token, workerId, toolType, toolCode)
            .map { it.toStorageRecord() }
    }


    override fun loadTransactionsByWorkerId( token:String,workerId: Long): Flow<List<Transaction>> {
        return flow {
            emit(apiService.loadTransactionsByWorkerId(token, workerId)
                .map { it.toTransaction() })
            updateTransactionsFlow.collect {
                emit(apiService.loadTransactionsByWorkerId(token, workerId)
                    .map { it.toTransaction() })
            }
        }
    }


    override suspend fun loadWorkersByDepartment( token:String,department: Department): List<Worker> {
        return apiService.loadWorkersByDepartment(token, department)
            .map { it.toWorker() }
    }

    override suspend fun loadStorageWorkerByDepartment( token:String,department: Department): Worker {
        return apiService.loadStorageWorkerByDepartment(token, department).toWorker()
    }


    override suspend fun checkLogin(login: String, password: String): Token {
        val userDTO =  UserDTOSignIn ( username = login, password = password )
        return apiService.checkLogin(userDTO).body() ?: throw IllegalArgumentException("Empty token")
    }

    override suspend fun createTransaction(
        token:String,
        sender: Worker,
        receiver: Worker,
        tool: Tool,
        amount: Int
    ): Transaction {
        val transaction = Transaction(
            sender = sender,
            receiver = receiver,
            tool = tool,
            amount = amount,
            date = LocalDate.now()
        )
        val commitTransaction = apiService.createTransaction(token, transaction.toTransactionWEB())
        return commitTransaction.toTransaction()
    }

    override suspend fun loadAmountByWorkerAndTool(token:String,worker: Worker, tool: Tool): Int {
        return apiService.loadAmountByWorkerAndTool(
            token,
            worker.id,
            tool.code
        )
    }

    override suspend fun loadToolsForSearch( token:String,code: String): List<Tool> {
        return apiService.loadToolsForSearch(token,code).map { it.toTool() }
    }

    override suspend fun loadTransactionsWithAnotherDepartment(
        token:String,
        anotherDepartment: Department,
        toolCode: String
    ): List<Transaction> {
        return apiService.loadTransactionsWithAnotherDepartment(token, anotherDepartment, toolCode)
            .map { it.toTransaction() }
    }

    override suspend fun updateTransactions() {
        updateTransactionsFlow.emit(Unit)
    }

    override suspend fun loadWorkerById(token: String, id: Long): Worker {
            return ApiFactory.apiService.loadWorkerById(token,id)
    }

}
