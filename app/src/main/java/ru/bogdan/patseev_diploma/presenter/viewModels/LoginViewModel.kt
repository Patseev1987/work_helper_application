package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import java.time.LocalDate

class LoginViewModel:ViewModel() {

    fun checkUser(login:String, password:String): Worker {
        if(login.startsWith("s")){
            return Worker(
                1,
                "Andrei",
                "Andreev",
                "Ivanich",
                LocalDate.now(),
                Department.DEPARTMENT_19,
                WorkerType.STORAGE_WORKER
            )
        } else {
            return Worker(
                1,
                "Ivan",
                "Ivanov",
                "Ivanich",
                LocalDate.now(),
                Department.DEPARTMENT_17,
                WorkerType.WORKER
            )
        }
    }
}