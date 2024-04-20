package ru.bogdan.patseev_diploma

import android.app.Application
import ru.bogdan.patseev_diploma.domain.models.Worker

class MyApplication: Application() {
    private var _worker: Worker? = null
    val worker:Worker get() = _worker!!

    private var _storageWorker:Worker? = null

    val storageWorker:Worker get() = _storageWorker!!
    fun setWorker(worker:Worker){
        _worker = worker
    }

    fun setStorageWorker(storageWorker:Worker){
        _storageWorker = storageWorker
    }
}