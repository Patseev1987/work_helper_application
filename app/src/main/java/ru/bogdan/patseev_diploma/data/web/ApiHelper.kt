package ru.bogdan.patseev_diploma.data.web


import kotlinx.coroutines.flow.Flow
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType


interface ApiHelper {

   suspend fun loadStorageRecordByWorkerId(workerId: Long, toolType: ToolType, toolCode:String = ""):List<StorageRecord>
    suspend fun checkLogin(login:String, password:String):Flow<Worker>
    suspend fun createTransaction(sender: Worker, receiver: Worker, tool: Tool, amount: Int):Transaction
}