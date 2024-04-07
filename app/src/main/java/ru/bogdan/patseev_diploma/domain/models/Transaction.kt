package ru.bogdan.patseev_diploma.domain.models

import ru.bogdan.patseev_diploma.domain.models.tools.Tool
import java.time.LocalDateTime

data class Transaction(
    val id:Long,
    val from:Worker,
    val destination: Worker,
    val date: LocalDateTime,
    val tool:Tool,
    val amount:Int,
    val note:String? = null
) {
}