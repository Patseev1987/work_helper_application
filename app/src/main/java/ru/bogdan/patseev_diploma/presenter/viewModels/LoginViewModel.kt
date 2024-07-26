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
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.domain.useCases.CheckLoginUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageWorkerByDepartmentUseCase
import ru.bogdan.patseev_diploma.presenter.states.LoginState
import ru.bogdan.patseev_diploma.util.CONNECTION_REFUSED
import ru.bogdan.patseev_diploma.util.NETWORK_UNREACHABLE
import ru.bogdan.patseev_diploma.util.TokenBundle
import java.net.ConnectException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val application: MyApplication,
    private val checkLoginUseCase: CheckLoginUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Waiting)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun checkLogin(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("TOKEN_TOKEN_TOKEN", "1")
                _state.value = LoginState.Loading
                val token = checkLoginUseCase(login, password)
                Log.d("TOKEN_TOKEN_TOKEN", token.token)
                TokenBundle.setToken(application,token.token)
                Log.d("TOKEN_TOKEN_TOKEN", "after setToken")
                Log.d("TOKEN_TOKEN_TOKEN","worker id -> "+TokenBundle.getWorkerId(application).toString())

               _state.value = LoginState.ConnectionProblem( token.token )
            } catch (e: RuntimeException) {
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
