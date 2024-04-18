package ru.bogdan.m17_recyclerview.data


import kotlinx.coroutines.flow.Flow
import ru.bogdan.patseev_diploma.domain.models.Worker


interface ApiHelper {
   // fun getPhotos(earthDate: String): Flow<List<PhotoByRover>>


    suspend fun checkLogin(login:String, password:String):Flow<Worker>
}