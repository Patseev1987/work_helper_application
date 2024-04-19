package ru.bogdan.patseev_diploma.data.web

import ru.bogdan.patseev_diploma.data.web.pojo.WorkerWEB
import ru.bogdan.patseev_diploma.domain.models.Worker
import java.time.LocalDate

class WorkerMapper {

    fun WorkerToWorkerWEB(worker: Worker): WorkerWEB {
        return WorkerWEB(
            id = worker.id,
            lastName = worker.secondName,
            firstName = worker.firstName,
            department = worker.department,
            patronymic = worker.patronymic,
            login = worker.login,
            password = worker.password,
            joinDate = worker.joinDate.toString(),
            type = worker.type,
        )
    }

    fun WorkerWEBToWorker(workerWEB: WorkerWEB): Worker {
        return Worker(
            id = workerWEB.id,
            secondName = workerWEB.lastName,
            firstName = workerWEB.firstName,
            type = workerWEB.type,
            joinDate = LocalDate.parse(workerWEB.joinDate),
            department = workerWEB.department,
            patronymic = workerWEB.patronymic,
            login = workerWEB.login,
            password = workerWEB.password,
        )
    }
}