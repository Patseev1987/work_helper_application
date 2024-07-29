package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.useCases.LoadWorkersByDepartmentUseCase
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewWorkerState
import ru.bogdan.patseev_diploma.util.TokenBundle
import javax.inject.Inject

@OptIn(FlowPreview::class)
class RecycleViewWorkersViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadWorkersByDepartmentUseCase: LoadWorkersByDepartmentUseCase,
    private val navController: NavController
) : ViewModel() {

    val tokenBundle = TokenBundle(application)

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
            try {
                _state.value = RecycleViewWorkerState.Loading
                workers = loadWorkersByDepartmentUseCase(
                    tokenBundle.getToken(),
                    tokenBundle.getDepartment()
                )
                _state.value = RecycleViewWorkerState.Result(workers)
            } catch (e: retrofit2.HttpException) {
                if (e.message?.trim() == HTTP_406) {
                    tokenBundle.returnToLoginFragment(
                        navController,
                        R.id.action_recycleViewWithWorkersFragment_to_loginFragment
                    )
                }
            } catch (e: Exception) {
                _state.value = RecycleViewWorkerState.ConnectionProblem(
                    application.getString(
                        R.string
                            .server_doesn_t_respond_try_again_a_little_bit_later
                    )
                )
            }
        }
    }

    private companion object {
        const val HTTP_406 = "HTTP 406"
    }
}