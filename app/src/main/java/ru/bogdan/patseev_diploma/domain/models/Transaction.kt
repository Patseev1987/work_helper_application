package ru.bogdan.patseev_diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class Transaction(
    val id: Long = 0L,
    val sender: Worker,
    val receiver: Worker,
    val date: LocalDate,
    val tool: Tool,
    val amount: Int,
) : Parcelable {
}