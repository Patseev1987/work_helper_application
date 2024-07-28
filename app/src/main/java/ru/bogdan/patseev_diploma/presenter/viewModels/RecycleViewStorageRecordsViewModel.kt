package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageRecordByWorkerIdUseCase
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewState
import ru.bogdan.patseev_diploma.util.TokenBundle
import java.time.LocalDate
import javax.inject.Inject


class RecycleViewStorageRecordsViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadStorageRecordByWorkerIdUseCase: LoadStorageRecordByWorkerIdUseCase
) : ViewModel() {

    private val tokenBundle = TokenBundle(application)

    private val searchString: MutableStateFlow<String> = MutableStateFlow(BLANK_TOOL_CODE)

    private val _state: MutableStateFlow<RecycleViewState> =
        MutableStateFlow(RecycleViewState.Loading)

    val state = _state.asStateFlow()

    private var worker: Worker = EMPTY_WORKER

    fun loadTools(position: Int, toolCode: String = BLANK_TOOL_CODE) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = RecycleViewState.Result(
                    loadStorageRecordByWorkerIdUseCase(
                        tokenBundle.getToken(),
                        worker.id,
                        position.getToolType(),
                        toolCode)
                )
            } catch (e: Exception) {
                Log.d("EXCEPTION_EXCEPTION", "RecycleViewStorageRecordsViewModel ${e.message}")
                _state.value = RecycleViewState.ConnectionProblem(
                    application.getString(
                        R.string
                            .server_doesn_t_respond_try_again_a_little_bit_later
                    )
                )
            }

        }
    }

    @OptIn(FlowPreview::class)
    fun updateToolsWithFilter(position: Int, toolCode: String) {
        searchString.value = toolCode
        viewModelScope.launch(Dispatchers.IO) {
            searchString.debounce(500)
                .collect { toolCode ->
                    _state.value = RecycleViewState.Loading
                    loadTools(position, toolCode)
                }
        }
    }

    private fun Int.getToolType(): ToolType {
        return when (this) {
            CUTTING_POSITION -> ToolType.CUTTING
            MEASURE_POSITION -> ToolType.MEASURE
            HELPERS_POSITION -> ToolType.HELPERS
            else -> throw RuntimeException("unknown tool type")
        }
    }

    fun setWorker(worker: Worker) {
        this.worker = worker
    }

    companion object {
        private const val CUTTING_POSITION = 0
        private const val MEASURE_POSITION = 1
        private const val HELPERS_POSITION = 2
        private const val BLANK_TOOL_CODE = ""
        private val EMPTY_WORKER = Worker(
            0,
            "",
            "",
            "",
            LocalDate.now(),
            Department.DEPARTMENT_19,
            WorkerType.WORKER,
            ""
        )
    }
}

