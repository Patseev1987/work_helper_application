package ru.bogdan.patseev_diploma.di

import dagger.Module
import dagger.Provides
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewStorageRecordsFragment


@Module
class SubModule {

    @Provides
    @Suppress("DEPRECATION")
    // min SDK lower than 33 (TIRAMISU)
    fun provideWorker(fragment: RecycleViewStorageRecordsFragment): Worker {
        return fragment.requireArguments().getParcelable(
            RecycleViewStorageRecordsFragment.WORKER
        )!!
    }
}