package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Transaction


sealed class RecycleVIewTransactionState {
    object Loading:RecycleVIewTransactionState()
    data class Result(val transaction:List<Transaction>):RecycleVIewTransactionState()
}