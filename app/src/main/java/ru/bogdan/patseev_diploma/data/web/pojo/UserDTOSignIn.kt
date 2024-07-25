package ru.bogdan.patseev_diploma.data.web.pojo

import com.google.gson.annotations.SerializedName

data class UserDTOSignIn(
    @SerializedName("username")
    val username:String,
    @SerializedName("password")
    val password:String,
)
