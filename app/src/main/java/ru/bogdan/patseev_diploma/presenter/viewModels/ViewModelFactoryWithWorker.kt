package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.domain.models.Worker
import java.lang.RuntimeException

class ViewModelFactoryWithWorker(
    private val worker: Worker
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecycleViewViewModel::class.java)) {
            return RecycleViewViewModel(worker) as T
        } else {
            throw RuntimeException("Unknown ViewModel-> $modelClass")
        }
    }
}