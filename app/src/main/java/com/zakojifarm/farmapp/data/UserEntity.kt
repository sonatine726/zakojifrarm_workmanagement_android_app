package com.zakojifarm.farmapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = UserDatabase.DB_NAME
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    val name: String,
    val explanation: String?
)
