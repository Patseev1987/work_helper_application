package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewState


class RecycleViewStorageRecordsViewModel(
    private val worker: Worker
) : ViewModel() {


    private val apiHelper = ApiHelperImpl(ApiFactory.apiService)

     val searchString: MutableStateFlow<String> = MutableStateFlow(BLANK_TOOL_CODE)

    private val _state: MutableStateFlow<RecycleViewState> =
        MutableStateFlow(RecycleViewState.Loading)

    val state = _state.asStateFlow()

   fun loadTools(position: Int, toolCode: String = BLANK_TOOL_CODE) {
        viewModelScope.launch {
          _state.value = RecycleViewState.Result(
              apiHelper.loadStorageRecordByWorkerId(worker.id, position.getToolType(), toolCode)
          )
        }
    }

    @OptIn(FlowPreview::class)
    fun updateToolsWithFilter(position: Int, toolCode:String) {
        searchString.value = toolCode
        viewModelScope.launch(Dispatchers.IO) {
            searchString.debounce(500)
                .collect { toolCode ->
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

    companion object {
        private const val CUTTING_POSITION = 0
        private const val MEASURE_POSITION = 1
        private const val HELPERS_POSITION = 2
        private const val BLANK_TOOL_CODE = ""
    }
}

