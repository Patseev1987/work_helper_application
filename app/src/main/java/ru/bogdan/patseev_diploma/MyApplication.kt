package ru.bogdan.patseev_diploma

import android.app.Application
import ru.bogdan.patseev_diploma.domain.models.Worker

class MyApplication: Application() {
    private var _worker: Worker? = null
    val worker:Worker get() = _worker!!

    fun setWorker(worker:Worker){
        _worker = worker
    }
}