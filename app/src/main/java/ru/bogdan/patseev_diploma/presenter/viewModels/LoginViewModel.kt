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
import ru.bogdan.patseev_diploma.domain.useCases.CheckLoginUseCase
import ru.bogdan.patseev_diploma.presenter.states.LoginState
import ru.bogdan.patseev_diploma.util.TokenBundle
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val application: MyApplication,
    private val checkLoginUseCase: CheckLoginUseCase,
) : ViewModel() {
    private val tokenBundle = TokenBundle(application)
    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Waiting)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun checkLogin(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = LoginState.Loading
                val token = checkLoginUseCase(login, password)
                tokenBundle.setToken(token.token)
                val workerType = tokenBundle.getWorkerType()
                Log.d("TOKEN_TOKEN_TOKEN",tokenBundle.getToken())
               _state.value = LoginState.LoginResult( workerType = workerType)
            } catch (e: RuntimeException) {
                Log.d("TOKEN_TOKEN_TOKEN",e.message.toString())
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
