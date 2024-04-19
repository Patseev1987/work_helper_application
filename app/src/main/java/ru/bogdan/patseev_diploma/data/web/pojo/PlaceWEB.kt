package ru.bogdan.patseev_diploma.data.web.pojo


import com.google.gson.annotations.SerializedName

data class PlaceWEB(
    @SerializedName("column")
    val column: String,
    @SerializedName("row")
    val row: String,
    @SerializedName("shelf")
    val shelf: String
)