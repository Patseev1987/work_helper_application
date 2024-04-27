package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
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

    fun updateTransactions(){
        viewModelScope.launch {
            apiHelperImpl.updateTransactions()
        }
    }
}