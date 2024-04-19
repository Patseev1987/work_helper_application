package ru.bogdan.patseev_diploma.data.web.pojo


import com.google.gson.annotations.SerializedName

data class ToolWEB(
    @SerializedName("additionalInfo")
    val additionalInfo: String?,
    @SerializedName("code")
    val code: String,
    @SerializedName("controlDate")
    val controlDate: String?,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("place")
    val place: PlaceWEB,
    @SerializedName("type")
    val type: String
)