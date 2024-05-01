package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.StorageRecord

sealed class RecycleViewState {
    object Loading:RecycleViewState()
    data class Result(val records:List<StorageRecord>):RecycleViewState()
    data class ConnectionProblem(val message:String):RecycleViewState()

}