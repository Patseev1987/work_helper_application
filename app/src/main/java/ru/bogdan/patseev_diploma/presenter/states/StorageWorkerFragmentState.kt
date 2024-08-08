package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker

sealed class StorageWorkerFragmentState {
    data object Loading : StorageWorkerFragmentState()
    data class ResultsTransaction(
        val transactions: List<Transaction>,
        val worker: Worker,
        val sharpen:Worker,
        val storageOfDecommissionedTools:Worker
    ):StorageWorkerFragmentState()

    data class ConnectionProblem(val message: String) : StorageWorkerFragmentState()
}