package ru.bogdan.patseev_diploma.di

import dagger.BindsInstance
import dagger.Component
import dagger.Component.Factory
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.presenter.fragments.LoginFragment
import ru.bogdan.patseev_diploma.presenter.fragments.TabLayoutFragment


@ApplicationScope
@Component(modules = [DataModule::class, ViewModelsModule::class])
interface ApplicationComponent {

    fun inject(loginFragment: LoginFragment)

    fun inject(tabLayoutFragment: TabLayoutFragment)

    fun getSubComponentFactory():SubComponentApplication.Factory

    @Factory
    interface ComponentFactory {
        fun create(@BindsInstance application: MyApplication): ApplicationComponent
    }
}