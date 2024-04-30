package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.presenter.states.CameraFragmentState
import java.lang.RuntimeException
import javax.inject.Inject

class CameraFragmentViewModel @Inject constructor(): ViewModel() {
    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    private val _state: MutableStateFlow<CameraFragmentState> =
        MutableStateFlow(CameraFragmentState.Waiting)

    private var _tool:Tool? = null
    val tool:Tool
        get() {
            _tool?.let {
                return it
            }
            throw RuntimeException ("tool wasn't defined")
        }

    val state = _state.asStateFlow()

    private fun checkToolCode(inputString: String): Boolean {
        val toolCodeRegex = """[a-zA-Z0-9]{3,8}-[a-zA-Z0-9]{4,10}""".toRegex()
        return inputString.matches(toolCodeRegex)
    }

    fun getTool(inputString: String, worker:Worker) {
        if (checkToolCode(inputString)) {
            viewModelScope.launch(Dispatchers.IO) {
                _state.value = CameraFragmentState.Result(
                    apiHelperImpl.loadToolsForSearch(inputString).first(),
                    showButton(worker)
                )
                _tool = apiHelperImpl.loadToolsForSearch(inputString).first()
            }
        } else {
            _state.value = CameraFragmentState.Error(UNKNOWN_CODE)
        }
    }


    private fun showButton(worker:Worker):Boolean{
        return worker.type == WorkerType.STORAGE_WORKER
    }
    companion object {
        private const val UNKNOWN_CODE = "unknown code"
    }
}