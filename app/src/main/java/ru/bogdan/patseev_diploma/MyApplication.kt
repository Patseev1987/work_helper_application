package ru.bogdan.patseev_diploma

import android.app.Application
import ru.bogdan.patseev_diploma.di.DaggerApplicationComponent
import ru.bogdan.patseev_diploma.domain.models.Worker

class MyApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}