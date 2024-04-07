package ru.bogdan.patseev_diploma.domain.models.tools

import ru.bogdan.patseev_diploma.domain.models.enums.ToolType

 data class CuttingTool(
     override val code: String,
     override val name: String,
     override val description: String,
     override val notes:String? = null,
     val type:ToolType,
     override val icon:String
    ): Tool(code, name, description, notes,icon) {
}