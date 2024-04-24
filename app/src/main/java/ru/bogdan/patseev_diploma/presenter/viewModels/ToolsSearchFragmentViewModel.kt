package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bogdan.m17_recyclerview.data.ApiFactory
import ru.bogdan.m17_recyclerview.data.ApiHelperImpl
import ru.bogdan.patseev_diploma.presenter.states.FragmentSearchToolsState
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewState

@OptIn(FlowPreview::class)
class ToolsSearchFragmentViewModel:ViewModel() {
    private val apiHelper = ApiHelperImpl(ApiFactory.apiService)

    private val _state:MutableStateFlow<FragmentSearchToolsState> = MutableStateFlow(FragmentSearchToolsState.Loading)
    val state = _state.asStateFlow()

    val searchString: MutableStateFlow<String> = MutableStateFlow("")

    init{
        viewModelScope.launch {
            searchString.debounce(500)
                .filter { it.length > 3 }
                .collect{
                    loadTools(it)
                }
        }

    }

    private fun loadTools(code:String){
        viewModelScope.launch {
            _state.value = FragmentSearchToolsState.Loading
            val tools = apiHelper.loadToolsFrSearch(code)
            _state.value = FragmentSearchToolsState.Result(tools)
        }
    }

}