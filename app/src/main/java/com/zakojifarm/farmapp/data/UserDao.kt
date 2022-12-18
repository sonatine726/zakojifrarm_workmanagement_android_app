package com.zakojifarm.farmapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserDao> {
    @Query("SELECT * FROM users WHERE id = :id")
    fun get(id: Int): Flow<UserEntity?>

    @Query("SELECT * FROM users")
    fun selectAll(): Flow<List<UserEntity>>
}