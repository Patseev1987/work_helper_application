package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import ru.bogdan.patseev_diploma.presenter.states.CameraFragmentState
import javax.inject.Inject

class CameraFragmentViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadToolsForSearchUseCase: LoadToolsForSearchUseCase,
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

    fun getTool(inputString: String, worker: Worker) {
        if (checkToolCode(inputString)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
//                    _state.value = CameraFragmentState.Result(
//                        loadToolsForSearchUseCase(inputString).first(),
//                        showButton(worker)
//                    )
//                    _tool = loadToolsForSearchUseCase(inputString).first()
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