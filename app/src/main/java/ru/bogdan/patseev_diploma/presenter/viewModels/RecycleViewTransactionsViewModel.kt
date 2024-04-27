package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewTransactionFragment
import ru.bogdan.patseev_diploma.presenter.states.RecycleVIewTransactionState
import java.lang.RuntimeException

class RecycleViewTransactionsViewModel(
    private val application: MyApplication,
    private val mode: Int
) : ViewModel() {

    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    val state: StateFlow<RecycleVIewTransactionState> =
        when (mode) {
            RecycleViewTransactionFragment.DECOMMISSIONED_TOOLS_MODE -> {
                apiHelperImpl
                    .loadTransactionsWithDecommissionedTools(application.worker.department)
                    .onStart { RecycleVIewTransactionState.Loading }
                    .map {
                        RecycleVIewTransactionState.Result(it,
                            getMessageForTitle(mode)) as RecycleVIewTransactionState
                    }.stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.Lazily,
                        initialValue = RecycleVIewTransactionState.Loading
                    )
            }

            RecycleViewTransactionFragment.FROM_SHARPEN_MODE -> {
                apiHelperImpl
                    .loadTransactionsWithToolFromSharpen(application.worker.department)
                    .onStart { RecycleVIewTransactionState.Loading }
                    .map {
                        RecycleVIewTransactionState.Result(it,
                            getMessageForTitle(mode)) as RecycleVIewTransactionState
                    }.stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.Lazily,
                        initialValue = RecycleVIewTransactionState.Loading
                    )
            }
            RecycleViewTransactionFragment.TO_SHARPEN_MODE -> {
                apiHelperImpl
                    .loadTransactionsWithToolToSharpen(application.worker.department)
                    .onStart { RecycleVIewTransactionState.Loading }
                    .map {
                        RecycleVIewTransactionState.Result(it,
                            getMessageForTitle(mode)) as RecycleVIewTransactionState
                    }.stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.Lazily,
                        initialValue = RecycleVIewTransactionState.Loading
                    )
            }
            else -> throw RuntimeException("Unknown mode for RecycleViewTransactionFragment")
        }


    private fun getMessageForTitle(mode:Int):String{
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
}