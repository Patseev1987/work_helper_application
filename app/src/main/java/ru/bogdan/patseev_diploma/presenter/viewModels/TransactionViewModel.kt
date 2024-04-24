package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogdan.m17_recyclerview.data.ApiFactory
import ru.bogdan.m17_recyclerview.data.ApiHelperImpl
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.states.TransactionState

class TransactionViewModel : ViewModel() {
    private var sender: Worker? = null
    private var receiver: Worker? = null
    private var tool: Tool? = null

    private val apiHelperImpl = ApiHelperImpl(ApiFactory.apiService)

    private val _state: MutableStateFlow<TransactionState> =
        MutableStateFlow(TransactionState.Loading)
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

    fun checkParam() {
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


    //we should paste delay between states i don't know why
    fun doTransaction(amount: Int) {
        viewModelScope.launch {
            _state.value = TransactionState.Loading
            delay(1000)
            _state.value = TransactionState.Waiting
            delay(10)
            _state.value = TransactionState.Error("asdasd")
            delay(10)
            _state.value = TransactionState.Loading
            delay(1000)
            _state.value =TransactionState.Waiting
        }

    }
}




