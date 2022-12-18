package com.zakojifarm.farmapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao : BaseDao<EventEntity> {
    @Query("SELECT * FROM ${EventDatabase.DB_NAME} WHERE id = :id")
    fun get(id: Int): Flow<EventEntity?>

    @Query("SELECT * FROM ${EventDatabase.DB_NAME}")
    fun selectAll(): Flow<List<EventEntity>>

    @Query("SELECT * FROM ${EventDatabase.DB_NAME} WHERE user_id = :userId ORDER BY time ASC")
    fun selectAllOfSpecificUser(userId: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM ${EventDatabase.DB_NAME} WHERE user_id = :userId ORDER BY time ASC Limit :limit")
    fun selectAllOfSpecificUser(userId: Long, limit: Int): Flow<List<EventEntity>>

    @Query(
        "SELECT * FROM ${UserDatabase.DB_NAME} " +
                "JOIN ${EventDatabase.DB_NAME} ON ${UserDatabase.DB_NAME}.id = ${EventDatabase.DB_NAME}.user_id"
    )
    fun selectAllUsersAndEvents(): Map<UserEntity, List<EventEntity>>
}