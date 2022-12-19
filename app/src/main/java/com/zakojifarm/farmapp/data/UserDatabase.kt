package com.zakojifarm.farmapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zakojifarm.farmapp.MainApplication

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DB_NAME = "users"

        private var instance: UserDatabase? = null
        private const val DB_FILE_NAME = "$DB_NAME.fb"

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                MainApplication.instance.applicationContext,
                UserDatabase::class.java, DB_FILE_NAME
            ).build().also {
                instance = it
            }
        }
    }
}