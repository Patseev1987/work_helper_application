package ru.bogdan.patseev_diploma.di

import dagger.BindsInstance
import dagger.Component
import dagger.Component.Factory
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.presenter.fragments.CameraFragment
import ru.bogdan.patseev_diploma.presenter.fragments.LoginFragment
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewStorageRecordsFragment
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewTransactionFragment
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewWithWorkersFragment
import ru.bogdan.patseev_diploma.presenter.fragments.StorageWorkerFragment
import ru.bogdan.patseev_diploma.presenter.fragments.TabLayoutFragment
import ru.bogdan.patseev_diploma.presenter.fragments.ToolsForSearchFragment
import ru.bogdan.patseev_diploma.presenter.fragments.TransactionFragment
import ru.bogdan.patseev_diploma.presenter.fragments.WorkerFragment

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelsModule::class])
interface ApplicationComponent {
    fun inject(cameraFragment: CameraFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(recycleViewTransactionFragment: RecycleViewTransactionFragment)
    fun inject(recycleViewWithWorkersFragment: RecycleViewWithWorkersFragment)
    fun inject(storageWorkerFragment: StorageWorkerFragment)
    fun inject(tabLayoutFragment: TabLayoutFragment)
    fun inject(toolsForSearchFragment: ToolsForSearchFragment)
    fun inject(transactionFragment: TransactionFragment)
    fun inject(workerFragment: WorkerFragment)
    fun inject(recycleViewStorageRecordsFragment: RecycleViewStorageRecordsFragment)

    @Factory
    interface ComponentFactory {
        fun create(@BindsInstance application: MyApplication): ApplicationComponent
    }
}