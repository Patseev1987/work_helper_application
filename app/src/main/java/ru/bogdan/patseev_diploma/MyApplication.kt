package ru.bogdan.patseev_diploma

import android.app.Application
import ru.bogdan.patseev_diploma.domain.models.Worker

class MyApplication: Application() {
    var worker: Worker? = null
}