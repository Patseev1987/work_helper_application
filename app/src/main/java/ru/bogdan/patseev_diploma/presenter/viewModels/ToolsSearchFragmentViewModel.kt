package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.useCases.LoadToolsForSearchUseCase
import ru.bogdan.patseev_diploma.presenter.states.FragmentSearchToolsState
import ru.bogdan.patseev_diploma.util.BLANK_TOOL_CODE
import ru.bogdan.patseev_diploma.util.TokenBundle
import javax.inject.Inject


@OptIn(FlowPreview::class)
class ToolsSearchFragmentViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadToolsForSearchUseCase: LoadToolsForSearchUseCase
) : ViewModel() {

   private val tokenBundle = TokenBundle(application)

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
                val tools = loadToolsForSearchUseCase(tokenBundle.getToken(), code)
                _state.value = FragmentSearchToolsState.Result(tools)
            } catch (e: Exception) {
                Log.d("EXCEPTION_EXCEPTION", "ToolSearchViewModel ${e.message}")
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