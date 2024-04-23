package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.bogdan.m17_recyclerview.data.ApiFactory
import ru.bogdan.m17_recyclerview.data.ApiHelperImpl
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState


class StorageWorkerViewModel(private val application: MyApplication):ViewModel() {

    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    val state: StateFlow<StorageWorkerFragmentState> = apiHelperImpl
        .loadTransactionsByWorkerId(application.worker.id)
        .onStart { StorageWorkerFragmentState.Loading }
        .map{
            StorageWorkerFragmentState.ResultsTransaction(it) as StorageWorkerFragmentState
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = StorageWorkerFragmentState.Loading
        )

}