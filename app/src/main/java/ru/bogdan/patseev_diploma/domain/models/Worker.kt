package ru.bogdan.patseev_diploma.domain.models

import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import java.time.LocalDateTime

data class Worker(
    val id:Long,
    val firstName:String,
    val secondName:String,
    val patronymic:String,
    val joinDate: LocalDateTime,
    val department: Department,
    val type:WorkerType
) {
}