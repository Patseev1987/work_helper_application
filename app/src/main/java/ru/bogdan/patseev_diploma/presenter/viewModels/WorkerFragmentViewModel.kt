package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
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
import ru.bogdan.patseev_diploma.presenter.states.WorkerFragmentState


class WorkerFragmentViewModel(private val application: MyApplication) : ViewModel() {

    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    val state: StateFlow<WorkerFragmentState> = apiHelperImpl
        .loadTransactionsByWorkerId(application.worker.id)
        .onStart { WorkerFragmentState.Loading }
        .map{
            WorkerFragmentState.ResultsTransaction(it) as WorkerFragmentState
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = WorkerFragmentState.Loading
        )
}