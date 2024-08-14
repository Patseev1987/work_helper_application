package ru.bogdan.patseev_diploma.di

import androidx.navigation.NavController
import dagger.BindsInstance
import dagger.Subcomponent
import ru.bogdan.patseev_diploma.presenter.fragments.*

@Subcomponent(modules = [ViewModelsModuleWithParam::class])
interface SubComponentApplication {
    fun inject(recycleViewWithWorkersFragment: RecycleViewWithWorkersFragment)
    fun inject(cameraFragment: CameraFragment)
    fun inject(recycleViewStorageRecordsFragment: RecycleViewStorageRecordsFragment)
    fun inject(recycleViewTransactionFragment: RecycleViewTransactionFragment)
    fun inject(storageWorkerFragment: StorageWorkerFragment)
    fun inject(toolsForSearchFragment: ToolsForSearchFragment)
    fun inject(transactionFragment: TransactionFragment)
    fun inject(workerFragment: WorkerFragment)
    fun inject(recordSearchByToolCodeFragment: RecordSearchByToolCodeFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navController: NavController): SubComponentApplication
    }
}