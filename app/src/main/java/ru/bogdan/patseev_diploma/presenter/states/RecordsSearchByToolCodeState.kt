package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Worker

sealed class RecordsSearchByToolCodeState() {
    data object Loading : RecordsSearchByToolCodeState()
    data object Waiting : RecordsSearchByToolCodeState()
    data class Result(val records: List<StorageRecord>) : RecordsSearchByToolCodeState()
    data class ConnectionProblem(val message: String) : RecordsSearchByToolCodeState()
}