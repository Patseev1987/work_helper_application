package ru.bogdan.m17_recyclerview.data


import kotlinx.coroutines.flow.Flow



interface ApiHelper {
   // fun getPhotos(earthDate: String): Flow<List<PhotoByRover>>

    suspend fun updatePhotos()
}