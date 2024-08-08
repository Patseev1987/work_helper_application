package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker

sealed class WorkerFragmentState {
    data object Loading : WorkerFragmentState()
    data class Results(
        val transactions: List<Transaction>,
        val worker: Worker,
        val storageWorker:Worker
    ) : WorkerFragmentState()
    data class ConnectionProblem(val message: String) : WorkerFragmentState()
}