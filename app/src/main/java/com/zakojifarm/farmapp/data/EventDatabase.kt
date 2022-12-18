package com.zakojifarm.farmapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zakojifarm.farmapp.MainApplication

@Database(entities = [EventEntity::class, UserEntity::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): UserDao

    companion object {
        const val DB_NAME = "events"

        private var instance: EventDatabase? = null
        private const val DB_FILE_NAME = "$DB_NAME.fb"

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                MainApplication.instance.applicationContext,
                EventDatabase::class.java, DB_FILE_NAME
            ).build().also {
                instance = it
            }
        }
    }
}