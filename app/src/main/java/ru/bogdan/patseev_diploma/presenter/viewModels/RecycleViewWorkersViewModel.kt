package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.useCases.LoadWorkersByDepartmentUseCase
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewWorkerState
import javax.inject.Inject

@OptIn(FlowPreview::class)
class RecycleViewWorkersViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadWorkersByDepartmentUseCase: LoadWorkersByDepartmentUseCase
) : ViewModel() {

    val searchString: MutableStateFlow<String> = MutableStateFlow("")

    private val _state: MutableStateFlow<RecycleViewWorkerState> =
        MutableStateFlow(RecycleViewWorkerState.Loading)
    val state = _state.asStateFlow()

    private var workers: List<Worker> = listOf()

    init {
        loadingWorkers()
        viewModelScope.launch {
            searchString
                .debounce(500)
                .collect {
                    _state.value = RecycleViewWorkerState.Loading
                    _state.value = RecycleViewWorkerState.Result(
                        workers.filter { worker: Worker ->
                            worker.secondName.lowercase().contains(it.lowercase())
                        }
                    )
                }
        }
    }

    private fun loadingWorkers() {
        viewModelScope.launch {
            _state.value = RecycleViewWorkerState.Loading
            workers = loadWorkersByDepartmentUseCase(
                application.worker.department
            )
            _state.value = RecycleViewWorkerState.Result(workers)
        }

    }


}