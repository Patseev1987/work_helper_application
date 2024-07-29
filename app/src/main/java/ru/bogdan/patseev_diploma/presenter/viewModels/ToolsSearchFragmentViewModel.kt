package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.useCases.LoadToolsForSearchUseCase
import ru.bogdan.patseev_diploma.presenter.states.FragmentSearchToolsState
import ru.bogdan.patseev_diploma.util.BLANK_TOOL_CODE
import ru.bogdan.patseev_diploma.util.HTTP_406
import ru.bogdan.patseev_diploma.util.TokenBundle
import javax.inject.Inject


@OptIn(FlowPreview::class)
class ToolsSearchFragmentViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadToolsForSearchUseCase: LoadToolsForSearchUseCase,
    private val navController : NavController,
    private val tokenBundle:TokenBundle
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
                val tools = loadToolsForSearchUseCase(tokenBundle.getToken(), code)
                _state.value = FragmentSearchToolsState.Result(tools)
            } catch (e: retrofit2.HttpException) {
                if (e.message?.trim() == HTTP_406) {
                    tokenBundle.returnToLoginFragment(
                        navController,
                        R.id.action_toolsFragmentForSearchFragment_to_loginFragment
                    )
                }
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