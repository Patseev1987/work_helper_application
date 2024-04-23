package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Worker

sealed class RecycleViewWorkerState() {
    object Loading:RecycleViewWorkerState()
    data class Result(val workers:List<Worker>):RecycleViewWorkerState()
}