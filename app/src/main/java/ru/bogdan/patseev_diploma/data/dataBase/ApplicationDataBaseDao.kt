package ru.bogdan.patseev_diploma.data.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDataBaseDao {
//
//    @Insert
//    suspend fun insertPhoto(photo: PhotoEntity)
//
//    @Query("select * from my_application_photos")
//    fun getPhotos(): Flow<List<PhotoEntity>>
//
//    @Query("delete from my_application_photos where uri = :uri")
//    suspend fun deletePhoto(uri:String)
}