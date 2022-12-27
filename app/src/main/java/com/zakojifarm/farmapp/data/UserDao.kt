package com.zakojifarm.farmapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM ${UserDatabase.DB_NAME} WHERE id = :id")
    fun get(id: Int): Flow<UserEntity?>

    @Query("SELECT * FROM ${UserDatabase.DB_NAME}")
    fun selectAll(): Flow<List<UserEntity>>

    @Query(
        "SELECT * FROM ${UserDatabase.DB_NAME} " +
                "INNER JOIN ${EventDatabase.DB_NAME} ON ${UserDatabase.DB_NAME}.id = ${EventDatabase.DB_NAME}.user_id"
    )
    fun selectUserAndEvents(): Flow<Map<UserEntity, List<EventEntity>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventIgnore(event: EventEntity)

    @Update
    fun updateEvent(event: EventEntity)

    @Update
    fun updateEvents(events: List<EventEntity>)

    @Delete
    fun deleteEvent(event: EventEntity)
}