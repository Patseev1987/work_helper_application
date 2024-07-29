package ru.bogdan.patseev_diploma.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.bogdan.patseev_diploma.presenter.viewModels.CameraFragmentViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.LoginViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewStorageRecordsViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewTransactionsViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.RecycleViewWorkersViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.StorageWorkerViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.TabLayoutVieModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ToolsSearchFragmentViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.TransactionViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.WorkerFragmentViewModel


@Module
interface ViewModelsModuleWithParam {


    @Binds
    @ViewModelKey(RecycleViewWorkersViewModel::class)
    @IntoMap
    fun bindRecycleViewWorkersViewModel(
        recycleViewWorkersViewModel: RecycleViewWorkersViewModel
    ): ViewModel


}