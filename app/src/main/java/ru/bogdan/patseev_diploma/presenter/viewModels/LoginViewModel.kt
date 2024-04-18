package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogdan.m17_recyclerview.data.ApiFactory
import ru.bogdan.patseev_diploma.data.web.WorkerMapper
import ru.bogdan.patseev_diploma.presenter.states.LoginState

class LoginViewModel : ViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Waiting)
    val state: StateFlow<LoginState> = _state.asStateFlow()
    private val apiService = ApiFactory.apiService
    private val mapper = WorkerMapper()


    fun checkLogin(login: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            Log.d("Tag", apiService.checkLogin(login, password).toString())
            val worker = mapper.WorkerWEBToWorker(apiService.checkLogin(login, password))
            if (worker.login.isNotEmpty()) {
                _state.value = LoginState.LoginResult(worker)
            } else {
                _state.value = LoginState.Error
            }
        }
    }
}