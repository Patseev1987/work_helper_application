package ru.bogdan.patseev_diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.bogdan.patseev_diploma.domain.models.tools.Tool
import java.time.LocalDateTime
@Parcelize
data class Transaction(
    val id:Long,
    val from:Worker,
    val destination: Worker,
    val date: LocalDateTime,
    val tool:Tool,
    val amount:Int,
    val note:String? = null
): Parcelable {
}