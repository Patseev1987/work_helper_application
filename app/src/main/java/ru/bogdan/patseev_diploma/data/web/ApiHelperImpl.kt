package ru.bogdan.m17_recyclerview.data


import android.util.Log
import kotlinx.coroutines.flow.*

import javax.inject.Inject
import kotlin.math.log

class ApiHelperImpl @Inject constructor(

    private val apiService: ApiService,
    private val flow: MutableSharedFlow<Unit>,
    private var page: Int
) : ApiHelper {
//
//    override fun getPhotos(earthDate: String): Flow<List<PhotoByRover>> {
//        return flow {
//            emit(
//                mapper.listWithPhotoFromNASAToListWithPhotoByRover(apiService.loadPhotos(earthDate, page))
//            )
//            flow.collect {
//                emit(mapper.listWithPhotoFromNASAToListWithPhotoByRover(apiService.loadPhotos(earthDate, ++page)))
//            }
//        }
//    }

    override suspend fun updatePhotos() {
        flow.emit(Unit)
    }
}
