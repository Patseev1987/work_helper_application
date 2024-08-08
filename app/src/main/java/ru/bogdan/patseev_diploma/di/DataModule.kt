package ru.bogdan.patseev_diploma.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.data.ApplicationRepositoryImpl
import ru.bogdan.patseev_diploma.data.web.ApiFactory
import ru.bogdan.patseev_diploma.data.web.ApiHelper
import ru.bogdan.patseev_diploma.data.web.ApiHelperImpl
import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import ru.bogdan.patseev_diploma.presenter.fragments.RecycleViewStorageRecordsFragment
import ru.bogdan.patseev_diploma.util.TokenBundle


@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindApplicationRepository(impl: ApplicationRepositoryImpl): ApplicationRepository

    @ApplicationScope
    @Binds
    fun bindApiHelper(impl: ApiHelperImpl): ApiHelper

    companion object {

        @Provides
        fun provideApiService() = ApiFactory.apiService

        @Provides
        fun provideTokenBundle(application: MyApplication): TokenBundle {
            return TokenBundle(application)
        }
    }
}