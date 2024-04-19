package ru.bogdan.patseev_diploma.data.web.pojo


import com.google.gson.annotations.SerializedName

data class StorageRecordWEB(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("id")
    val id: Long,
    @SerializedName("tool")
    val tool: ToolWEB,
    @SerializedName("worker")
    val worker: WorkerWEB
)