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
interface ViewModelsModule {
    @Binds
    @ViewModelKey(CameraFragmentViewModel::class)
    @IntoMap
    fun bindCameraFragmentViewModel(cameraFragmentViewModel: CameraFragmentViewModel): ViewModel

    @Binds
    @ViewModelKey(LoginViewModel::class)
    @IntoMap
    fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @ViewModelKey(RecycleViewStorageRecordsViewModel::class)
    @IntoMap
    fun bindRecycleViewStorageRecordsViewModel(
        recycleViewStorageRecordsViewModel: RecycleViewStorageRecordsViewModel
    ): ViewModel

    @Binds
    @ViewModelKey(RecycleViewTransactionsViewModel::class)
    @IntoMap
    fun bindRecycleViewTransactionsViewModel(
        recycleViewTransactionsViewModel: RecycleViewTransactionsViewModel
    ): ViewModel

    @Binds
    @ViewModelKey(RecycleViewWorkersViewModel::class)
    @IntoMap
    fun bindRecycleViewWorkersViewModel(
        recycleViewWorkersViewModel: RecycleViewWorkersViewModel
    ): ViewModel

    @Binds
    @ViewModelKey(StorageWorkerViewModel::class)
    @IntoMap
    fun bindStorageWorkerViewModel(
        storageWorkerViewModel: StorageWorkerViewModel
    ): ViewModel

    @Binds
    @ViewModelKey(TabLayoutVieModel::class)
    @IntoMap
    fun bindTabLayoutVieModel(
        tabLayoutVieModel: TabLayoutVieModel
    ): ViewModel

    @Binds
    @ViewModelKey(ToolsSearchFragmentViewModel::class)
    @IntoMap
    fun bindToolsSearchFragmentViewModel(
        toolsSearchFragmentViewModel: ToolsSearchFragmentViewModel
    ): ViewModel

    @Binds
    @ViewModelKey(TransactionViewModel::class)
    @IntoMap
    fun bindTransactionViewModel(
        transactionViewModel: TransactionViewModel
    ): ViewModel

    @Binds
    @ViewModelKey(WorkerFragmentViewModel::class)
    @IntoMap
    fun bindWorkerFragmentViewModel(
        workerFragmentViewModel: WorkerFragmentViewModel
    ): ViewModel

}