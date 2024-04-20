package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogdan.m17_recyclerview.data.ApiFactory
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.data.web.mappers.toWorker
import ru.bogdan.patseev_diploma.presenter.states.LoginState

class LoginViewModel(private val application: MyApplication):ViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Waiting)
    val state: StateFlow<LoginState> = _state.asStateFlow()
    private val apiService = ApiFactory.apiService



    fun checkLogin(login: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            Log.d("Tag", apiService.checkLogin(login, password).toString())
            val worker = apiService.checkLogin(login, password).toWorker()
            if (worker.login.isNotEmpty()) {
                application.setWorker(worker)
                _state.value = LoginState.LoginResult(worker)
            } else {
                _state.value = LoginState.Error
            }
        }
    }
}
