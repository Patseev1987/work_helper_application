package ru.bogdan.patseev_diploma.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.bogdan.patseev_diploma.presenter.viewModels.LoginViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.TabLayoutVieModel


@Module
interface ViewModelsModule {

    @Binds
    @ViewModelKey(LoginViewModel::class)
    @IntoMap
    fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @ViewModelKey(TabLayoutVieModel::class)
    @IntoMap
    fun bindTabLayoutVieModel(
        tabLayoutVieModel: TabLayoutVieModel
    ): ViewModel

}