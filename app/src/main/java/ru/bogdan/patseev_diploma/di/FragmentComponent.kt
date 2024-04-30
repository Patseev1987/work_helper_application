package ru.bogdan.patseev_diploma.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewStorageRecordsFragment

@Subcomponent(modules = [ViewModelsModule::class,SubModule::class])
interface FragmentComponent {

     fun inject(fragment: RecycleViewStorageRecordsFragment)

     @Subcomponent.Factory
     interface Factory{
         fun create(
             @BindsInstance fragment: RecycleViewStorageRecordsFragment
         ):FragmentComponent
     }
}