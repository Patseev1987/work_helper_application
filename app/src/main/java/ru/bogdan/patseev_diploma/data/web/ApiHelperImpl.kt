package ru.bogdan.m17_recyclerview.data


import android.util.Log
import kotlinx.coroutines.flow.*
import ru.bogdan.patseev_diploma.data.web.mappers.toStorageRecord
import ru.bogdan.patseev_diploma.data.web.mappers.toTool
import ru.bogdan.patseev_diploma.data.web.mappers.toTransaction
import ru.bogdan.patseev_diploma.data.web.mappers.toWorker
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import java.time.LocalDate
import ru.bogdan.patseev_diploma.domain.models.Transaction as Transaction1

class ApiHelperImpl(
    private val apiService: ApiService,
) : ApiHelper {

    private val updateFlow: MutableSharedFlow<Unit> = MutableSharedFlow()
    override fun loadStorageRecordByWorkerId(workerId: Long): Flow<List<StorageRecord>> {
        return flow {
            emit(apiService.loadStorageRecordsByWorkerId(workerId)
                .map { it.toStorageRecord() })
            updateFlow.collect {
                emit(apiService.loadStorageRecordsByWorkerId(workerId)
                    .map { it.toStorageRecord() })
            }
        }
    }

    fun loadTransactionsByWorkerId(workerId:Long):Flow<List<Transaction1>>{
        return flow {
            emit( apiService.loadTransactionsByWorkerId(workerId)
                .map{ it.toTransaction()})
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

    override suspend fun createTransaction(sender: Worker, receiver: Worker, tool: Tool, amount: Int) {
        val transaction = Transaction1(
            sender=sender,
            receiver = receiver,
            tool = tool,
            amount = amount,
            date = LocalDate.now()
        )
        apiService.createTransaction(transaction)
    }


    suspend fun loadToolsFrSearch(code:String):List<Tool>{
        return apiService.loadToolsForSearch(code).map{it.toTool()}
    }
    suspend fun updateStorageRecords() {
        updateFlow.emit(Unit)
    }

}
