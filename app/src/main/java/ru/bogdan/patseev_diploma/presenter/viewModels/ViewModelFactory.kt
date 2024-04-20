package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.bogdan.patseev_diploma.MyApplication
import java.lang.RuntimeException

class ViewModelFactory(private val application: MyApplication):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TabLayoutVieModel::class.java)) {
            return TabLayoutVieModel(application = application) as T
        } else if (modelClass.isAssignableFrom(RecycleViewViewModel::class.java)) {
            return RecycleViewViewModel(application) as T
        }else if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(application) as T
        } else {
            throw RuntimeException("Unknown ViewModel-> $modelClass")
        }
    }
}