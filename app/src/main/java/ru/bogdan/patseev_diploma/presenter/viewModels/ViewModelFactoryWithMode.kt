package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.bogdan.patseev_diploma.MyApplication
import java.lang.RuntimeException

class ViewModelFactoryWithMode(
    private val application: MyApplication,
    private val mode: Int
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecycleViewTransactionsViewModel::class.java)){
            return RecycleViewTransactionsViewModel(application,mode) as T
        }else {
            throw RuntimeException("Unknown ViewModel")
        }
    }
}