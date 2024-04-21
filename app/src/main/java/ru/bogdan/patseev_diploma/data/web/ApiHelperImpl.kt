package ru.bogdan.m17_recyclerview.data


import android.util.Log
import kotlinx.coroutines.flow.*
import ru.bogdan.patseev_diploma.data.web.mappers.toStorageRecord
import ru.bogdan.patseev_diploma.data.web.mappers.toTransaction
import ru.bogdan.patseev_diploma.data.web.mappers.toWorker
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker

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

    fun loadTransactionsByWorkerId(workerId:Long):Flow<List<Transaction>>{
        return flow {
            emit( apiService.loadTransactionsByWorkerId(workerId)
                .map{ it.toTransaction()})
        }
    }



    override suspend fun checkLogin(login: String, password: String): Flow<Worker> {
        return flow {

            emit(apiService.checkLogin(login, password).toWorker())
        }
    }

    suspend fun updateStorageRecords() {
        updateFlow.emit(Unit)
    }

}
