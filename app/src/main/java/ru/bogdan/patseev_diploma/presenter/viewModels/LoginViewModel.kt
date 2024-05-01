package ru.bogdan.patseev_diploma.presenter.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.domain.useCases.CheckLoginUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageWorkerByDepartmentUseCase
import ru.bogdan.patseev_diploma.presenter.states.LoginState
import ru.bogdan.patseev_diploma.util.CONNECTION_REFUSED
import ru.bogdan.patseev_diploma.util.NETWORK_UNREACHABLE
import java.net.ConnectException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val application: MyApplication,
    private val checkLoginUseCase: CheckLoginUseCase,
    private val loadStorageWorkerByDepartmentUseCase: LoadStorageWorkerByDepartmentUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Waiting)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun checkLogin(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = LoginState.Loading
                val worker = checkLoginUseCase(login, password)
                if (worker.login.isNotEmpty()) {
                    application.setWorker(worker)
                    if (worker.type != WorkerType.STORAGE_WORKER) {
                        val storageWorker =
                            loadStorageWorkerByDepartmentUseCase(worker.department)
                        application.setStorageWorker(storageWorker);
                    }
                    _state.value = LoginState.LoginResult(worker)
                } else {
                    _state.value = LoginState.Error
                }
            } catch (e: Exception) {
                        _state.value = LoginState.ConnectionProblem(
                            application.getString(
                                R.string
                                    .server_doesn_t_respond_try_again_a_little_bit_later
                            )
                        )
            }
        }
    }
}
