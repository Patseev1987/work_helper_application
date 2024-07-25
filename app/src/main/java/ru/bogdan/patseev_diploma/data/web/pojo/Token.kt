package ru.bogdan.patseev_diploma.data.web.pojo


import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token")
    val token: String
)