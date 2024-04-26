package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import java.lang.RuntimeException

class ViewModelFactoryWithApplication(
    private val application: MyApplication,
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TabLayoutVieModel::class.java)) {
            return TabLayoutVieModel(application = application) as T
        }else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application) as T
        }else if (modelClass.isAssignableFrom(WorkerFragmentViewModel::class.java)) {
            return WorkerFragmentViewModel(application) as T
        }else if (modelClass.isAssignableFrom(StorageWorkerViewModel::class.java)) {
            return StorageWorkerViewModel(application) as T
        }else if (modelClass.isAssignableFrom(RecycleViewWorkersViewModel::class.java)) {
            return RecycleViewWorkersViewModel(application) as T
        }else if (modelClass.isAssignableFrom(TransactionViewModel::class.java)){
            return TransactionViewModel(application) as T
        } else {
            throw RuntimeException("Unknown ViewModel-> $modelClass")
        }
    }
}