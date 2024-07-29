package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.domain.useCases.LoadToolsForSearchUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadWorkerByIdUseCase
import ru.bogdan.patseev_diploma.presenter.states.CameraFragmentState
import ru.bogdan.patseev_diploma.util.HTTP_406
import ru.bogdan.patseev_diploma.util.TokenBundle
import javax.inject.Inject

class CameraFragmentViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadToolsForSearchUseCase: LoadToolsForSearchUseCase,
    private val navController: NavController,
    private val tokenBundle: TokenBundle,
    private val loadWorkerByIdUseCase: LoadWorkerByIdUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<CameraFragmentState> =
        MutableStateFlow(CameraFragmentState.Waiting)

    private var _tool: Tool? = null
    val tool: Tool
        get() {
            _tool?.let {
                return it
            }
            throw RuntimeException("tool wasn't defined")
        }

    val state = _state.asStateFlow()

    private fun checkToolCode(inputString: String): Boolean {
        return inputString.matches(REGEX)
    }

    fun getTool(inputString: String) {
        if (checkToolCode(inputString)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val worker = loadWorkerByIdUseCase(
                        token = tokenBundle.getToken(),
                        id = tokenBundle.getWorkerId()
                    )
                    _state.value = CameraFragmentState.Result(
                        tool = loadToolsForSearchUseCase(
                            token = tokenBundle.getToken(),
                            code = inputString
                        ).first(),
                        isShowButtons = showButton(worker),
                        worker = worker
                    )
                    _tool = loadToolsForSearchUseCase(
                        token = tokenBundle.getToken(),
                        code = inputString
                    ).first()
                } catch (e: retrofit2.HttpException) {
                    if (e.message?.trim() == HTTP_406) {
                        tokenBundle.returnToLoginFragment(
                            navController,
                            R.id.action_recycleViewWithWorkersFragment_to_loginFragment
                        )
                    }
                } catch (e: Exception) {
                    _state.value = CameraFragmentState.ConnectionProblem(
                        application.getString(
                            R.string
                                .server_doesn_t_respond_try_again_a_little_bit_later
                        )
                    )
                }
            }
        } else {
            _state.value = CameraFragmentState.Error(UNKNOWN_CODE)
        }
    }

    private fun showButton(worker: Worker): Boolean {
        return worker.type == WorkerType.STORAGE_WORKER
    }

    companion object {
        private const val UNKNOWN_CODE = "unknown code"
        private val REGEX = """[a-zA-Z0-9]{3,8}-[a-zA-Z0-9]{4,10}""".toRegex()
    }
}