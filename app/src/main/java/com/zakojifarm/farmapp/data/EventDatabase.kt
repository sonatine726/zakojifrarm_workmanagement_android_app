package com.zakojifarm.farmapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventEntity::class, UserEntity::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "events"
        const val DB_FILE_NAME = "$DB_NAME.fb"
    }
}