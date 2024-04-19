package ru.bogdan.patseev_diploma.data.web.pojo


import com.google.gson.annotations.SerializedName

data class TransactionWEB(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("id")
    val id: Long,
    @SerializedName("receiver")
    val receiver: WorkerWEB,
    @SerializedName("sender")
    val sender: WorkerWEB,
    @SerializedName("tool")
    val tool: ToolWEB
)