package ru.bogdan.patseev_diploma.util

import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import ru.bogdan.patseev_diploma.domain.models.tools.CuttingTool
import ru.bogdan.patseev_diploma.domain.models.tools.Place

fun main() {
    val cutter = CuttingTool(
        "6161-1212",
        "Cutter",
        "This Cutter for turing equipment",
        null,
        ToolType.CUTTING,
        "https://iconduck.com/icons/21805/cog",
        Place("1", "2", "3")
    )

    println(cutter)
}