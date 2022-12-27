package com.zakojifarm.farmapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, EventEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DB_NAME = "users"
        const val DB_FILE_NAME = "$DB_NAME.fb"
    }
}