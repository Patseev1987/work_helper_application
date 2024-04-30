package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.mappers.toWorker
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.domain.useCases.CheckLoginUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageWorkerByDepartmentUseCase
import ru.bogdan.patseev_diploma.presenter.states.LoginState
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
            } catch (e: ConnectException) {
                when {
                    e.cause?.message.toString().contains(NETWORK_UNREACHABLE) -> {
                        _state.value = LoginState.ConnectionProblem(
                            application.getString(
                                R.string
                                    .network_unreachable_check_your_internet_connection
                            )
                        )
                    }

                    e.cause?.message.toString().contains(CONNECTION_REFUSED) -> {
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
    }

    companion object {
        private const val NETWORK_UNREACHABLE = "Network is unreachable"
        private const val CONNECTION_REFUSED = "Connection refused"
    }
}
