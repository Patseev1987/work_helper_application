package ru.bogdan.patseev_diploma.di

import androidx.navigation.NavController
import dagger.BindsInstance
import dagger.Subcomponent
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewWithWorkersFragment

@Subcomponent(modules = [ViewModelsModuleWithParam::class])
interface SubComponentApplication {
    fun inject(recycleViewWithWorkersFragment: RecycleViewWithWorkersFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navController: NavController): SubComponentApplication
    }
}