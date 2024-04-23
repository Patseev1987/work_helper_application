package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Transaction

sealed class StorageWorkerFragmentState {
    object Loading:StorageWorkerFragmentState()
    data class ResultsTransaction(val transactions:List<Transaction>):StorageWorkerFragmentState()
}