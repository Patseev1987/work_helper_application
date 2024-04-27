package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewTransactionFragment
import ru.bogdan.patseev_diploma.presenter.states.RecycleVIewTransactionState
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewState
import java.lang.RuntimeException

class RecycleViewTransactionsViewModel(
    private val application: MyApplication,
    private val mode: Int
) : ViewModel() {

    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    private val searchString: MutableStateFlow<String> =
        MutableStateFlow(BLANK_TOOL_CODE)

    private val _state: MutableStateFlow<RecycleVIewTransactionState> =
        MutableStateFlow(RecycleVIewTransactionState.Loading)

    val state = _state.asStateFlow()


    fun loadTransactions(mode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (mode) {
                RecycleViewTransactionFragment.DECOMMISSIONED_TOOLS_MODE -> {
                    _state.value = RecycleVIewTransactionState.Result(
                        apiHelperImpl
                            .loadTransactionsWithDecommissionedTools(application.worker.department),
                        mode.getMessageForTitle()
                    )
                }

                RecycleViewTransactionFragment.FROM_SHARPEN_MODE -> {
                    _state.value = RecycleVIewTransactionState.Result(
                        apiHelperImpl
                            .loadTransactionsWithToolFromSharpen(application.worker.department),
                        mode.getMessageForTitle()
                    )
                }

                RecycleViewTransactionFragment.TO_SHARPEN_MODE -> {
                    _state.value = RecycleVIewTransactionState.Result(
                        apiHelperImpl
                            .loadTransactionsWithToolToSharpen(application.worker.department),
                        mode.getMessageForTitle()
                    )
                }

                else -> throw RuntimeException("Unknown mode for RecycleViewTransactionFragment")
            }
        }
    }

    private fun Int.getMessageForTitle(): String {
        return when (mode) {
            RecycleViewTransactionFragment.TO_SHARPEN_MODE -> application
                .getString(R.string.transactions_with_tools_to_sharpen)

            RecycleViewTransactionFragment.FROM_SHARPEN_MODE -> application
                .getString(R.string.transaction_with_tools_from_sharpen)

            RecycleViewTransactionFragment.DECOMMISSIONED_TOOLS_MODE -> application
                .getString(R.string.transactions_with_decommissioned_tools)

            else -> throw RuntimeException("Unknown mode")
        }
    }

    companion object {
        private const val BLANK_TOOL_CODE = ""
    }
}