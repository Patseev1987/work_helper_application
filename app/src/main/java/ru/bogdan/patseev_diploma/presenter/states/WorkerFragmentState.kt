package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Transaction

sealed class WorkerFragmentState {
    object Loading : WorkerFragmentState()
    data class ResultsTransaction(val transactions: List<Transaction>) : WorkerFragmentState()
    data class ConnectionProblem(val message: String) : WorkerFragmentState()
}