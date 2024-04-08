package ru.bogdan.patseev_diploma.domain.models.tools

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
@Parcelize
class OtherTool(
    override val  code: String,
    override val  name: String,
    override val  description: String,
    override val  notes: String?,
    val type:ToolType = ToolType.HELPERS,
    override val icon:String,
    override val place: Place
): Tool(code, name, description, notes, icon,place), Parcelable {
}