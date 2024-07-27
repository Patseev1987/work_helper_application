package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageWorkerByDepartmentUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadTransactionsByWorkerIdUseCase
import ru.bogdan.patseev_diploma.domain.useCases.UpdateTransactionUseCase
import ru.bogdan.patseev_diploma.presenter.states.LoginState
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import ru.bogdan.patseev_diploma.util.CONNECTION_REFUSED
import ru.bogdan.patseev_diploma.util.NETWORK_UNREACHABLE
import ru.bogdan.patseev_diploma.util.TokenBundle
import java.lang.RuntimeException
import java.net.ConnectException
import javax.inject.Inject


class StorageWorkerViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadStorageWorkerByDepartmentUseCase: LoadStorageWorkerByDepartmentUseCase,
    private val loadTransactionsByWorkerIdUseCase: LoadTransactionsByWorkerIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

   private val tokenBundle = TokenBundle(application)

    private var _sharpen: Worker? = null
    val sharpen: Worker get() = _sharpen ?: throw RuntimeException("_sharpen = null")
    private var _storageOfDecommissionedTools: Worker? = null
    val storageOfDecommissionedTools: Worker
        get() = _storageOfDecommissionedTools
            ?: throw RuntimeException("_storageOfDecommissionedTools = null")

    private val loadingFlow: MutableSharedFlow<StorageWorkerFragmentState> = MutableSharedFlow()

    val state: StateFlow<StorageWorkerFragmentState> = loadTransactionsByWorkerIdUseCase(
        tokenBundle.getToken(),
        application.worker.id
    )
        .onStart { StorageWorkerFragmentState.Loading }
        .map {
            StorageWorkerFragmentState.ResultsTransaction(it) as StorageWorkerFragmentState
        }
        .catch {
            loadingFlow.emit(
                StorageWorkerFragmentState.ConnectionProblem(
                    application.getString(
                        R.string
                            .server_doesn_t_respond_try_again_a_little_bit_later
                    )
                )
            )
        }
        .mergeWith(loadingFlow)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = StorageWorkerFragmentState.Loading
        )


    init {
        viewModelScope.launch {
            try {
                _sharpen = loadStorageWorkerByDepartmentUseCase(
                    tokenBundle.getToken(),
                    Department.SHARPENING)
                _storageOfDecommissionedTools =
                    loadStorageWorkerByDepartmentUseCase(
                        tokenBundle.getToken(),
                        Department.STORAGE_OF_DECOMMISSIONED_TOOLS)
            } catch (e: Exception) {
                loadingFlow.emit(
                    StorageWorkerFragmentState.ConnectionProblem(
                        application.getString(
                            R.string
                                .server_doesn_t_respond_try_again_a_little_bit_later
                        )
                    )
                )
            }
        }
    }

    private fun <T> Flow<T>.mergeWith(anotherFlow: Flow<T>): Flow<T> {
        return merge(this, anotherFlow)
    }

    fun updateTransactions() {
        viewModelScope.launch {
            updateTransactionUseCase()
        }
    }

}