package ru.bogdan.patseev_diploma.domain.models.tools

import ru.bogdan.patseev_diploma.domain.models.enums.ToolType

class OtherTool(
    override val  code: String,
    override val  name: String,
    override val  description: String,
    override val  notes: String?,
    val type:ToolType = ToolType.HELPERS,
    override val icon:String
): Tool(code, name, description, notes, icon) {
}