package ru.bogdan.patseev_diploma.data.web.pojo


import com.google.gson.annotations.SerializedName
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import java.time.LocalDate

data class WorkerWEB(
    @SerializedName("department")
    val department: Department ,
    @SerializedName("firstName")
    val firstName: String ,
    @SerializedName("id")
    val id: Long ,
    @SerializedName("joinDate")
    val joinDate: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("login")
    val login: String ,
    @SerializedName("patronymic")
    val patronymic: String,
    @SerializedName("type")
    val type: WorkerType
)