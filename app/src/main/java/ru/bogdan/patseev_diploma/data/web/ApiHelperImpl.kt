package ru.bogdan.patseev_diploma.data.web



import kotlinx.coroutines.flow.*
import ru.bogdan.patseev_diploma.data.web.mappers.toStorageRecord
import ru.bogdan.patseev_diploma.data.web.mappers.toTool
import ru.bogdan.patseev_diploma.data.web.mappers.toTransaction
import ru.bogdan.patseev_diploma.data.web.mappers.toTransactionWEB
import ru.bogdan.patseev_diploma.data.web.mappers.toWorker
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import java.time.LocalDate

class ApiHelperImpl(
    private val apiService: ApiService,
) : ApiHelper {

    private val updateTransactionsFlow: MutableSharedFlow<Unit> = MutableSharedFlow()
    override fun loadStorageRecordByWorkerId(workerId: Long): Flow<List<StorageRecord>> {
        return flow {
            emit(apiService.loadStorageRecordsByWorkerId(workerId)
                .map { it.toStorageRecord() })
        }
    }

    fun loadTransactionsByWorkerId(workerId:Long):Flow<List<Transaction>>{
        return flow {
            emit( apiService.loadTransactionsByWorkerId(workerId)
                .map{ it.toTransaction()})
            updateTransactionsFlow.collect{
                emit( apiService.loadTransactionsByWorkerId(workerId)
                    .map{ it.toTransaction()})
            }
        }
    }


    suspend fun loadWorkersByDepartment(department: Department):List<Worker>{
        return apiService.loadWorkersByDepartment(department)
            .map { it.toWorker() }
    }

   suspend fun loadStorageWorkerByDepartment(department: Department):Worker{
       return apiService.loadStorageWorkerByDepartment(department).toWorker()
   }



    override suspend fun checkLogin(login: String, password: String): Flow<Worker> {
        return flow {
            emit(apiService.checkLogin(login, password).toWorker())
        }
    }

    override suspend fun createTransaction(
        sender: Worker,
        receiver: Worker,
        tool: Tool,
        amount: Int
    ):Transaction {
        val transaction = Transaction(
            sender=sender,
            receiver = receiver,
            tool = tool,
            amount = amount,
            date = LocalDate.now()
        )
       val commitTransaction = apiService.createTransaction(transaction.toTransactionWEB())
        return commitTransaction.toTransaction()
    }

    suspend fun loadAmountByWorkerAndTool(worker:Worker, tool:Tool):Int{
        return apiService.loadAmountByWorkerAndTool(
            worker.id,
            tool.code
        )
    }
    suspend fun loadToolsFrSearch(code:String):List<Tool>{
        return apiService.loadToolsForSearch(code).map{it.toTool()}
    }

     fun loadTransactionsWithDecommissionedTools(senderDepartment:Department, page:Int = 0):Flow<List<Transaction>>{
        return flow{
           emit (
               apiService.loadTransactionsWithDecommissionedTools(senderDepartment,page)
               .map { it.toTransaction() }
           )
        }
    }

    fun loadTransactionsWithToolFromSharpen(receiverDepartment:Department, page:Int = 0):Flow<List<Transaction>>{
        return flow{
            emit (
                apiService.loadTransactionsWithToolFromSharpen(receiverDepartment,page)
                    .map { it.toTransaction() }
            )
        }
    }

     fun loadTransactionsWithToolToSharpen(senderDepartment:Department, page:Int = 0):Flow<List<Transaction>>{
        return flow{
            emit(
                apiService.loadTransactionsWithToolToSharpen(senderDepartment,page)
                    .map { it.toTransaction() }
            )
        }
    }

   suspend fun updateTransactions(){
        updateTransactionsFlow.emit(Unit)
    }
}
