package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bogdan.m17_recyclerview.data.ApiFactory
import ru.bogdan.m17_recyclerview.data.ApiHelperImpl
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import java.lang.RuntimeException


class StorageWorkerViewModel(private val application: MyApplication) : ViewModel() {

    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    private var _sharpen: Worker? = null
    val sharpen: Worker get() = _sharpen ?: throw RuntimeException("_sharpen = null")
    private var _storageOfDecommissionedTools: Worker? = null
    val storageOfDecommissionedTools: Worker
        get() = _storageOfDecommissionedTools
            ?: throw RuntimeException("_storageOfDecommissionedTools = null")

    init {
        viewModelScope.launch {
            _sharpen = apiHelperImpl.loadStorageWorkerByDepartment(Department.SHARPENING)
            _storageOfDecommissionedTools =
                apiHelperImpl.loadStorageWorkerByDepartment(Department.STORAGE_OF_DECOMMISSIONED_TOOLS)
        }
    }

    val state: StateFlow<StorageWorkerFragmentState> = apiHelperImpl
        .loadTransactionsByWorkerId(application.worker.id)
        .onStart { StorageWorkerFragmentState.Loading }
        .map {
            StorageWorkerFragmentState.ResultsTransaction(it) as StorageWorkerFragmentState
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = StorageWorkerFragmentState.Loading
        )


}