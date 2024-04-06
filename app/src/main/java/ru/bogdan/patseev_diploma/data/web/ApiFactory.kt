package ru.bogdan.m17_recyclerview.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiFactory {
    private val BASE_URL = "https://10.10.0.101:1337/api"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}