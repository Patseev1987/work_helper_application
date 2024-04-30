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
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageWorkerByDepartmentUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadTransactionsByWorkerIdUseCase
import ru.bogdan.patseev_diploma.domain.useCases.UpdateTransactionUseCase
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import java.lang.RuntimeException
import javax.inject.Inject


class StorageWorkerViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadStorageWorkerByDepartmentUseCase: LoadStorageWorkerByDepartmentUseCase,
    private val loadTransactionsByWorkerIdUseCase: LoadTransactionsByWorkerIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    private var _sharpen: Worker? = null
    val sharpen: Worker get() = _sharpen ?: throw RuntimeException("_sharpen = null")
    private var _storageOfDecommissionedTools: Worker? = null
    val storageOfDecommissionedTools: Worker
        get() = _storageOfDecommissionedTools
            ?: throw RuntimeException("_storageOfDecommissionedTools = null")

    init {
        viewModelScope.launch {
            _sharpen = loadStorageWorkerByDepartmentUseCase(Department.SHARPENING)
            _storageOfDecommissionedTools =
                loadStorageWorkerByDepartmentUseCase(Department.STORAGE_OF_DECOMMISSIONED_TOOLS)
        }
    }

    val state: StateFlow<StorageWorkerFragmentState> = loadTransactionsByWorkerIdUseCase(
        application.worker.id
    )
        .onStart { StorageWorkerFragmentState.Loading }
        .map {
            StorageWorkerFragmentState.ResultsTransaction(it) as StorageWorkerFragmentState
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = StorageWorkerFragmentState.Loading
        )

    fun updateTransactions() {
        viewModelScope.launch {
            updateTransactionUseCase()
        }
    }

}