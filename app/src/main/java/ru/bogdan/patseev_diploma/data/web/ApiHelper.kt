package ru.bogdan.m17_recyclerview.data


import kotlinx.coroutines.flow.Flow
import ru.bogdan.m17_recyclerview.domain.PhotoByRover


interface ApiHelper {
    fun getPhotos(earthDate: String): Flow<List<PhotoByRover>>

    suspend fun updatePhotos()
}