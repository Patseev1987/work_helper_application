package ru.bogdan.patseev_diploma.data.dataBase

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = [], exportSchema = false, version = 1)
abstract class MyApplicationPhotosDatabase() //: RoomDatabase() {

//    abstract val photoDao: ApplicationDataBaseDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: MyApplicationPhotosDatabase? = null
//
//        fun getInstance(application: Application): MyApplicationPhotosDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        application,
//                        MyApplicationPhotosDatabase::class.java,
//                        "patseev_diploma_database"
//                    ).build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    }

//}
