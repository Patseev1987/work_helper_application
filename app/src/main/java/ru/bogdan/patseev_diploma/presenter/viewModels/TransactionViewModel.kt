package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.states.TransactionState

class TransactionViewModel(private val application: MyApplication) : ViewModel() {
    private var sender: Worker? = null
    private var receiver: Worker? = null
    private var tool: Tool? = null
    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)
    private val _state: MutableStateFlow<TransactionState> =
        MutableStateFlow(TransactionState.Waiting)
    val state = _state.asStateFlow()


    fun setReceiver(newReceiver: Worker) {
        receiver = newReceiver
        _state.value = TransactionState.ReceiverState(newReceiver)
        checkParam()
    }

    fun setSender(newSender: Worker) {
        sender = newSender
        _state.value = TransactionState.SenderState(newSender)
        checkParam()
    }

    fun setTool(newTool: Tool) {
        tool = newTool
        _state.value = TransactionState.ToolState(newTool)
        checkParam()
    }

    private fun checkParam() {
        viewModelScope.launch {
            delay(1)
            sender?.let {
                _state.value = TransactionState.SenderState(it)
            }
            delay(1)
            receiver?.let {
                _state.value = TransactionState.ReceiverState(it)
            }
            delay(1)
            tool?.let {
                _state.value = TransactionState.ToolState(it)
            }
        }
    }

    fun doTransaction(amount: Int) {
        viewModelScope.launch {
            _state.value = TransactionState.Loading
            delay(1)
            when {
                sender == null -> {
                    _state.value =
                        TransactionState.Error(
                            application
                                .getString(R.string.you_don_t_set_the_sender)
                        )
                    delay(1)
                    return@launch
                }

                receiver == null -> {
                    TransactionState.Error(
                        application
                            .getString(R.string.you_don_t_set_the_receiver)
                    )
                        .also { _state.value = it }
                    delay(1)
                    return@launch
                }

                tool == null -> {
                    _state.value =
                        TransactionState.Error(
                            application
                                .getString(R.string.you_don_t_set_the_tool)
                        )
                    delay(1)
                    return@launch
                }
            }
            val amountToolFromSender = apiHelperImpl.loadAmountByWorkerAndTool(sender!!, tool!!)
            if (amountToolFromSender == -1) {
                _state.value = TransactionState.Error(
                    application.getString(
                        R.string.doesn_t_have,
                        sender?.secondName,
                        tool?.code
                    )
                )
                return@launch
            } else {
                if (amountToolFromSender < amount) {
                    _state.value = TransactionState.Error(
                        application.getString(R.string.have_only, sender?.secondName) +
                                "$amountToolFromSender ${tool?.code}"
                    )
                    return@launch
                } else {
                    apiHelperImpl.createTransaction(sender!!, receiver!!, tool!!, amount)
                    _state.value = TransactionState.Result(sender!!, receiver!!, tool!!, amount)
                }
            }
        }
    }
}







