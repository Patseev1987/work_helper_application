package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker

sealed class TransactionState {
    data class Result(
        val sender: Worker,
        val receiver: Worker,
        val tool: Tool,
        val amount: Int
    ) : TransactionState()

    data class SenderState(val sender: Worker) : TransactionState()
    data class ReceiverState(val receiver: Worker) : TransactionState()
    data class ToolState(val tool: Tool) : TransactionState()

    data class Error(val errorMessage: String) : TransactionState()
    object Waiting : TransactionState()
    object Loading : TransactionState()

    data class ConnectionProblem(val message: String) : TransactionState()
}