package ru.bogdan.patseev_diploma.domain.models.tools

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import java.time.LocalDateTime

@Parcelize
data class Tool(
   val code:String,
   val name: String,
   val description: String,
   val notes: String? = null,
   val icon: String? = null,
   val place: Place,
   val type: ToolType,
   val controlDate: LocalDateTime? = null
): Parcelable {

}