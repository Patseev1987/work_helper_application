package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Tool

sealed class CameraFragmentState {
    data class Result(val tool: Tool,val isShowButtons:Boolean):CameraFragmentState()
    data class Error(val msg:String):CameraFragmentState()
    data object Waiting:CameraFragmentState()
    data class ConnectionProblem(val message:String):CameraFragmentState()
}