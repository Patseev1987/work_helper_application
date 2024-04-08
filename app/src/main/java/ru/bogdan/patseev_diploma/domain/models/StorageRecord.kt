package ru.bogdan.patseev_diploma.domain.models

import ru.bogdan.patseev_diploma.domain.models.tools.Tool

data class StorageRecord(
    val worker:Worker,
    val tool:Tool,
    val amount:Int
) {

}