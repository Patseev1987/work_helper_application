package ru.bogdan.patseev_diploma.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import ru.bogdan.patseev_diploma.domain.models.tools.CuttingTool
import ru.bogdan.patseev_diploma.domain.models.tools.MeasureTool
import ru.bogdan.patseev_diploma.domain.models.tools.Place
import ru.bogdan.patseev_diploma.domain.models.tools.Tool
import java.time.LocalDate
import java.time.LocalDateTime


fun Fragment.transaction(
    id: Long,
    from: Worker,
    destination: Worker,
    date: LocalDateTime,
    tool: Tool,
    amount: Int,
    note: String? = null
): Transaction {
    return Transaction( id, from, destination, date, tool, amount, note)
}

fun ViewModel.transaction(
    id: Long,
    from: Worker,
    destination: Worker,
    date: LocalDateTime,
    tool: Tool,
    amount: Int,
    note: String? = null
): Transaction {
    return Transaction( id, from, destination, date, tool, amount, note)
}



fun generateTools():List<Tool>{
    val tools = mutableListOf<Tool>()
    val cutter = CuttingTool(
        "6161-1212",
        "Cutter",
        "This Cutter for turing equipment",
        null,
        ToolType.CUTTING,
        "https://iconduck.com/icons/21805/cog",
        Place("1", "2", "3")
    )
    val cutter2 = CuttingTool(
        "6161-4040",
        "Cutter Spec",
        "This Cutter for turing equipment",
        null,
        ToolType.CUTTING,
        "https://iconduck.com/icons/21805/cog",
        Place("1", "2", "3")
    )
    val cutter3 = CuttingTool(
        "2020-1212",
        "Cutter Des",
        "This Cutter for turing equipment",
        null,
        ToolType.CUTTING,
        "https://static-00.iconduck.com/assets.00/settings-icon-982x1024-ly8qbieh.png",
        Place("1", "2", "3")
    )

    val measure = MeasureTool(
        "8700-0001",
        "measure tool",
        "This measure",
        "range 0-25",
        ToolType.MEASURE,
        LocalDate.now(),
        "https://uxwing.com/wp-content/themes/uxwing/download/buildings-architecture-real-estate/home-color-icon.png",
        Place("1", "2", "3")
    )


    tools.add(measure)
    tools.add(cutter)
    tools.add(cutter2)
    tools.add(cutter3)


    for (i in 0..99){
        tools.add(
            MeasureTool(
                "1111-11$i",
                "tool $i",
                "Bla bla bla",
                null,
                ToolType.MEASURE,
                LocalDate.now(),
                "https://uxwing.com/wp-content/themes/uxwing/download/buildings-architecture-real-estate/home-color-icon.png",
                Place(i.toString(),i.toString(),i.toString())
                )
        )
    }
    return tools
}


fun String.checkTool():Boolean{
    val regex = """"""

    val cutter = CuttingTool(
        "6161-1212",
        "Cutter",
        "This Cutter for turing equipment",
        null,
        ToolType.CUTTING,
        "https://iconduck.com/icons/21805/cog",
        Place("1", "2", "3")
    )

    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    val result = gson.toJson(cutter)

    println(result)
    return false
}



