package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.domain.models.Tool

sealed class FragmentSearchToolsState {
    object Loading:FragmentSearchToolsState()
    data class Result(val tools:List<Tool>):FragmentSearchToolsState()
}