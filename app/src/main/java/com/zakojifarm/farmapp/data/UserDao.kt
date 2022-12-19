package com.zakojifarm.farmapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM ${UserDatabase.DB_NAME} WHERE id = :id")
    fun get(id: Int): Flow<UserEntity?>

    @Query("SELECT * FROM ${UserDatabase.DB_NAME}")
    fun selectAll(): Flow<List<UserEntity>>
}