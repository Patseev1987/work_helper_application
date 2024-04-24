package ru.bogdan.m17_recyclerview.data


import kotlinx.coroutines.flow.Flow
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker


interface ApiHelper {

    fun loadStorageRecordByWorkerId(workerId:Long):Flow<List<StorageRecord>>


    suspend fun checkLogin(login:String, password:String):Flow<Worker>
    suspend fun createTransaction(sender: Worker, receiver: Worker, tool: Tool, amount: Int)
}