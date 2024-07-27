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
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageWorkerByDepartmentUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadTransactionsByWorkerIdUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadWorkerByIdUseCase
import ru.bogdan.patseev_diploma.domain.useCases.UpdateTransactionUseCase
import ru.bogdan.patseev_diploma.presenter.states.WorkerFragmentState
import ru.bogdan.patseev_diploma.util.TokenBundle
import javax.inject.Inject


class WorkerFragmentViewModel @Inject constructor(
    private val application: MyApplication,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val loadWorkerByIdUseCase: LoadWorkerByIdUseCase,
    private val loadTransactionsByWorkerIdUseCase: LoadTransactionsByWorkerIdUseCase,
    private val loadStorageWorkerByDepartmentUseCase: LoadStorageWorkerByDepartmentUseCase
) : ViewModel() {

    private val tokenBundle = TokenBundle(application)

    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    private val loadingFlow: MutableSharedFlow<WorkerFragmentState> = MutableSharedFlow()

    val state: StateFlow<WorkerFragmentState> = loadTransactionsByWorkerIdUseCase(
            tokenBundle.getToken(),
            tokenBundle.getWorkerId()
        )
        .onStart { WorkerFragmentState.Loading }
        .map {transactions ->
            val worker = loadWorkerByIdUseCase(tokenBundle.getToken(),tokenBundle.getWorkerId())
            val storageWorker = loadStorageWorkerByDepartmentUseCase(
                tokenBundle.getToken(),
                tokenBundle.getDepartment()
            )
            WorkerFragmentState.Results(
                transactions = transactions,
                worker = worker,
                storageWorker = storageWorker
            ) as WorkerFragmentState
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