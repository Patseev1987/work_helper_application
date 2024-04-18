package ru.bogdan.patseev_diploma.domain.models.tools

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType

@Parcelize
open class Tool(
   open val code:String,
   open val name: String,
   open val description: String,
   open val notes: String?,
   open val icon: String,
   open val place: Place,
   open val type:ToolType
): Parcelable {

}