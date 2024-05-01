package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.useCases.UpdateTransactionUseCase
import ru.bogdan.patseev_diploma.presenter.states.LoginState
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import ru.bogdan.patseev_diploma.presenter.states.WorkerFragmentState
import ru.bogdan.patseev_diploma.util.CONNECTION_REFUSED
import ru.bogdan.patseev_diploma.util.NETWORK_UNREACHABLE
import javax.inject.Inject


class WorkerFragmentViewModel @Inject constructor(
    private val application: MyApplication,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    private val loadingFlow: MutableSharedFlow<WorkerFragmentState> = MutableSharedFlow()

    val state: StateFlow<WorkerFragmentState> = apiHelperImpl
        .loadTransactionsByWorkerId(application.worker.id)
        .onStart { WorkerFragmentState.Loading }
        .map {
            WorkerFragmentState.ResultsTransaction(it) as WorkerFragmentState
        }
        .mergeWith(loadingFlow)
        .catch {
            loadingFlow.emit(
                WorkerFragmentState.ConnectionProblem(
                    application.getString(
                        R.string
                            .server_doesn_t_respond_try_again_a_little_bit_later
                    )
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = WorkerFragmentState.Loading
        )

    private fun <T> Flow<T>.mergeWith(anotherFlow: Flow<T>): Flow<T> {
        return merge(this, anotherFlow)
    }

    fun updateTransactions() {
        viewModelScope.launch {
            updateTransactionUseCase()
        }
    }
}