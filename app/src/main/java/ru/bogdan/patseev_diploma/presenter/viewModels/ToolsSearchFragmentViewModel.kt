package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.presenter.states.FragmentSearchToolsState
import javax.inject.Inject


@OptIn(FlowPreview::class)
class ToolsSearchFragmentViewModel @Inject constructor():ViewModel() {
    private val apiHelper = ApiHelperImpl(ApiFactory.apiService)

    private val _state:MutableStateFlow<FragmentSearchToolsState> = MutableStateFlow(FragmentSearchToolsState.Waiting)
    val state = _state.asStateFlow()

    val searchString: MutableStateFlow<String> = MutableStateFlow(BLANK_TOOL_CODE)

    init{
        viewModelScope.launch {
            searchString.debounce(500)
                .filter { it.length > 3 }
                .onCompletion {
                    FragmentSearchToolsState.Waiting
                }
                .collect{
                    loadTools(it)
                }
        }

    }

    private fun loadTools(code:String){
        viewModelScope.launch {
            _state.value = FragmentSearchToolsState.Loading
            val tools = apiHelper.loadToolsForSearch(code)
            _state.value = FragmentSearchToolsState.Result(tools)
        }
    }

    companion object {
        private const val BLANK_TOOL_CODE = ""
    }

}