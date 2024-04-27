package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Worker

sealed class LoginState {
    object Error:LoginState()
    object Waiting:LoginState()
    object Loading:LoginState()
    data class LoginResult(val worker: Worker): LoginState()
    data class ConnectionProblem(val message:String):LoginState()
}