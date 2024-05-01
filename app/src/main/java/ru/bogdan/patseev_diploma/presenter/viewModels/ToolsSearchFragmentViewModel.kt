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
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.domain.useCases.LoadToolsForSearchUseCase
import ru.bogdan.patseev_diploma.presenter.states.FragmentSearchToolsState
import ru.bogdan.patseev_diploma.presenter.states.LoginState
import ru.bogdan.patseev_diploma.util.BLANK_TOOL_CODE
import ru.bogdan.patseev_diploma.util.CONNECTION_REFUSED
import ru.bogdan.patseev_diploma.util.NETWORK_UNREACHABLE
import java.net.ConnectException
import javax.inject.Inject


@OptIn(FlowPreview::class)
class ToolsSearchFragmentViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadToolsForSearchUseCase: LoadToolsForSearchUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<FragmentSearchToolsState> =
        MutableStateFlow(FragmentSearchToolsState.Waiting)
    val state = _state.asStateFlow()

    val searchString: MutableStateFlow<String> = MutableStateFlow(BLANK_TOOL_CODE)

    init {
        viewModelScope.launch {
            searchString.debounce(500)
                .filter { it.length > 3 }
                .onCompletion {
                    FragmentSearchToolsState.Waiting
                }
                .collect {
                    loadTools(it)
                }
        }

    }

    private fun loadTools(code: String) {
        viewModelScope.launch {
            try {
                _state.value = FragmentSearchToolsState.Loading
                val tools = loadToolsForSearchUseCase(code)
                _state.value = FragmentSearchToolsState.Result(tools)
            } catch (e: Exception) {

                _state.value = FragmentSearchToolsState.ConnectionProblem(
                    application.getString(
                        R.string
                            .server_doesn_t_respond_try_again_a_little_bit_later
                    )
                )
            }
        }
    }

}