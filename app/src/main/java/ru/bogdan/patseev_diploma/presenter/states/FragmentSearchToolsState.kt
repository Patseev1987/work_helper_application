package ru.bogdan.patseev_diploma.presenter.states

import ru.bogdan.patseev_diploma.databinding.FragmentToolsForSearchBinding
import ru.bogdan.patseev_diploma.domain.models.Tool

sealed class FragmentSearchToolsState {
    object Loading : FragmentSearchToolsState()
    data class Result(val tools: List<Tool>) : FragmentSearchToolsState()
    object Waiting : FragmentSearchToolsState()
    data class ConnectionProblem(val message: String) : FragmentSearchToolsState()
}