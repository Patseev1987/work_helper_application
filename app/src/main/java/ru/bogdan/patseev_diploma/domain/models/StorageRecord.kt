package ru.bogdan.patseev_diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StorageRecord(
    val id: Long,
    val worker: Worker,
    val tool: Tool,
    val amount: Int
) : Parcelable {

}