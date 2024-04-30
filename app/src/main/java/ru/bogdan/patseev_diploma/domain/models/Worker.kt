package ru.bogdan.patseev_diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class Worker(
    val id: Long,
    val firstName: String,
    val secondName: String,
    val patronymic: String,
    val joinDate: LocalDate,
    val department: Department,
    val type: WorkerType,
    val login: String,
    val password: String
) : Parcelable {
}