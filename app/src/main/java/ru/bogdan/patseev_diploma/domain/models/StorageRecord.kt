package ru.bogdan.patseev_diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.bogdan.patseev_diploma.domain.models.tools.Tool
@Parcelize
data class StorageRecord(
    val worker:Worker,
    val tool:Tool,
    val amount:Int
):Parcelable {

}