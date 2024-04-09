package ru.bogdan.patseev_diploma.domain.models.tools

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import java.time.LocalDate
@Parcelize
class MeasureTool(
    override val code: String,
    override val  name: String,
    override val  description: String,
    override val  notes: String?,
    override val type: ToolType = ToolType.MEASURE,
    val dateNextCheck: LocalDate,
    override val icon:String,
    override val place: Place
) : Tool(code, name, description, notes,icon, place, type), Parcelable {
}