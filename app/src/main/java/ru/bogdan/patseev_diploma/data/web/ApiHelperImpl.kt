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
        workerId: Long,
        toolType: ToolType,
        toolCode: String
    ): List<StorageRecord> {
        return apiService.loadStorageRecordsByWorkerId(workerId, toolType, toolCode)
            .map { it.toStorageRecord() }
    }


    override fun loadTransactionsByWorkerId(workerId: Long): Flow<List<Transaction>> {
        return flow {
            emit(apiService.loadTransactionsByWorkerId(workerId)
                .map { it.toTransaction() })
            updateTransactionsFlow.collect {
                emit(apiService.loadTransactionsByWorkerId(workerId)
                    .map { it.toTransaction() })
            }
        }
    }


    override suspend fun loadWorkersByDepartment(department: Department): List<Worker> {
        return apiService.loadWorkersByDepartment(department)
            .map { it.toWorker() }
    }

    override suspend fun loadStorageWorkerByDepartment(department: Department): Worker {
        return apiService.loadStorageWorkerByDepartment(department).toWorker()
    }


    override suspend fun checkLogin(login: String, password: String): Token {
        val userDTO =  UserDTOSignIn ( username = login, password = password )
        return apiService.checkLogin(userDTO).body() ?: throw IllegalArgumentException("Empty token")
    }

    override suspend fun createTransaction(
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
        val commitTransaction = apiService.createTransaction(transaction.toTransactionWEB())
        return commitTransaction.toTransaction()
    }

    override suspend fun loadAmountByWorkerAndTool(worker: Worker, tool: Tool): Int {
        return apiService.loadAmountByWorkerAndTool(
            worker.id,
            tool.code
        )
    }

    override suspend fun loadToolsForSearch(code: String): List<Tool> {
        return apiService.loadToolsForSearch(code).map { it.toTool() }
    }

    override suspend fun loadTransactionsWithAnotherDepartment(
        anotherDepartment: Department,
        toolCode: String
    ): List<Transaction> {
        return apiService.loadTransactionsWithAnotherDepartment(anotherDepartment, toolCode)
            .map { it.toTransaction() }
    }

    override suspend fun updateTransactions() {
        updateTransactionsFlow.emit(Unit)
    }


}
