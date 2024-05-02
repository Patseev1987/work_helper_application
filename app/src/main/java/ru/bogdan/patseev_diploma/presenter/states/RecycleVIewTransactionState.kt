package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Transaction


sealed class RecycleVIewTransactionState {
    object Loading : RecycleVIewTransactionState()
    data class Result(val transactions: List<Transaction>, val message: String) :
        RecycleVIewTransactionState()

    data class ConnectionProblem(val message: String) : RecycleVIewTransactionState()
}