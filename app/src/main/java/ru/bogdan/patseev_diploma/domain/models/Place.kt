package ru.bogdan.patseev_diploma.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Place(
    val shelf: String,
    val column: String,
    val row: String,
) : Parcelable {
}