package ru.bogdan.patseev_diploma.domain.models.tools

import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import java.time.LocalDate

class MeasureTool(
    override val code: String,
    override val  name: String,
    override val  description: String,
    override val  notes: String?,
    val type: ToolType = ToolType.MEASURE,
    val dateNextCheck: LocalDate
) : Tool(code, name, description, notes) {
}