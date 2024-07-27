package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType

sealed class LoginState {
    data object Error : LoginState()
    data object Waiting : LoginState()
    data object Loading : LoginState()
    data class LoginResult(val workerType: WorkerType) : LoginState()
    data class ConnectionProblem(val message: String) : LoginState()
}