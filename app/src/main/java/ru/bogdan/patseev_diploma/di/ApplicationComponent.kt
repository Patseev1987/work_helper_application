package ru.bogdan.patseev_diploma.di

import dagger.Component
import ru.bogdan.patseev_diploma.presenter.fragments.CameraFragment
import ru.bogdan.patseev_diploma.presenter.fragments.LoginFragment
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewStorageRecordsFragment
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewTransactionFragment
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewWithWorkersFragment
import ru.bogdan.patseev_diploma.presenter.fragments.SenderReceiverFragment
import ru.bogdan.patseev_diploma.presenter.fragments.StorageWorkerFragment
import ru.bogdan.patseev_diploma.presenter.fragments.TabLayoutFragment
import ru.bogdan.patseev_diploma.presenter.fragments.ToolFragment
import ru.bogdan.patseev_diploma.presenter.fragments.ToolsForSearchFragment
import ru.bogdan.patseev_diploma.presenter.fragments.TransactionFragment
import ru.bogdan.patseev_diploma.presenter.fragments.WorkerFragment

@Component
interface ApplicationComponent {
    fun inject(cameraFragment: CameraFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(recycleViewStorageRecordsFragment: RecycleViewStorageRecordsFragment)
    fun inject(recycleViewTransactionFragment: RecycleViewTransactionFragment)
    fun inject(recycleViewWithWorkersFragment: RecycleViewWithWorkersFragment)
    fun inject(senderReceiverFragment: SenderReceiverFragment)
    fun inject(storageWorkerFragment: StorageWorkerFragment)
    fun inject(tabLayoutFragment: TabLayoutFragment)
    fun inject(toolFragment: ToolFragment)
    fun inject(toolsForSearchFragment: ToolsForSearchFragment)
    fun inject(transactionFragment: TransactionFragment)
    fun inject(workerFragment: WorkerFragment)
}