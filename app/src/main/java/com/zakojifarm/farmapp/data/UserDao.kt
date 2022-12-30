package com.zakojifarm.farmapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM ${UserDatabase.DB_NAME} WHERE id = :id")
    fun get(id: Int): Flow<UserEntity?>

    @Query("SELECT * FROM ${UserDatabase.DB_NAME}")
    fun selectAll(): List<UserEntity>

    @Query("SELECT * FROM ${UserDatabase.DB_NAME}")
    fun selectAllByFlow(): Flow<List<UserEntity>>

    @Query(
        "SELECT * FROM ${UserDatabase.DB_NAME} " +
                "INNER JOIN ${EventDatabase.DB_NAME} ON ${UserDatabase.DB_NAME}.id = ${EventDatabase.DB_NAME}.user_id"
    )
    fun selectUserAndEvents(): Flow<Map<UserEntity, List<EventEntity>>>

    @Query(
        "SELECT ${EventDatabase.DB_NAME}.event_id, ${EventDatabase.DB_NAME}.time, ${EventDatabase.DB_NAME}.kind, ${EventDatabase.DB_NAME}.work_kind, ${EventDatabase.DB_NAME}.user_id FROM ${UserDatabase.DB_NAME} " +
                "JOIN ${EventDatabase.DB_NAME} ON ${UserDatabase.DB_NAME}.id = ${EventDatabase.DB_NAME}.user_id " +
                "WHERE ${UserDatabase.DB_NAME}.id = :userId " +
                "ORDER BY ${EventDatabase.DB_NAME}.time DESC"
    )
    fun selectEventsOfUser(userId: Long): List<EventEntity>

    @Query(
        "SELECT ${EventDatabase.DB_NAME}.event_id, ${EventDatabase.DB_NAME}.time, ${EventDatabase.DB_NAME}.kind, ${EventDatabase.DB_NAME}.work_kind, ${EventDatabase.DB_NAME}.user_id FROM ${UserDatabase.DB_NAME} " +
                "JOIN ${EventDatabase.DB_NAME} ON ${UserDatabase.DB_NAME}.id = ${EventDatabase.DB_NAME}.user_id " +
                "WHERE ${UserDatabase.DB_NAME}.id = :userId " +
                "ORDER BY ${EventDatabase.DB_NAME}.time DESC"
    )
    fun selectEventsOfUserByFlow(userId: Long): Flow<List<EventEntity>>

    @Query(
        "SELECT ${EventDatabase.DB_NAME}.event_id, ${EventDatabase.DB_NAME}.time, ${EventDatabase.DB_NAME}.kind, ${EventDatabase.DB_NAME}.work_kind, ${EventDatabase.DB_NAME}.user_id FROM ${UserDatabase.DB_NAME} " +
                "JOIN ${EventDatabase.DB_NAME} ON ${UserDatabase.DB_NAME}.id = ${EventDatabase.DB_NAME}.user_id " +
                "WHERE ${UserDatabase.DB_NAME}.id = :userId " +
                "ORDER BY ${EventDatabase.DB_NAME}.time DESC " +
                "Limit :limit"
    )
    fun selectEventsOfUser(userId: Long, limit: Int): List<EventEntity>

    @Query(
        "SELECT ${EventDatabase.DB_NAME}.event_id, ${EventDatabase.DB_NAME}.time, ${EventDatabase.DB_NAME}.kind, ${EventDatabase.DB_NAME}.work_kind, ${EventDatabase.DB_NAME}.user_id FROM ${UserDatabase.DB_NAME} " +
                "JOIN ${EventDatabase.DB_NAME} ON ${UserDatabase.DB_NAME}.id = ${EventDatabase.DB_NAME}.user_id " +
                "WHERE ${UserDatabase.DB_NAME}.id = :userId " +
                "ORDER BY ${EventDatabase.DB_NAME}.time DESC " +
                "Limit :limit"
    )
    fun selectEventsOfUserByFlow(userId: Long, limit: Int): Flow<List<EventEntity>>

    @Query(
        "SELECT ${EventDatabase.DB_NAME}.event_id, ${EventDatabase.DB_NAME}.time, ${EventDatabase.DB_NAME}.kind, ${EventDatabase.DB_NAME}.work_kind, ${EventDatabase.DB_NAME}.user_id FROM ${UserDatabase.DB_NAME} " +
                "JOIN ${EventDatabase.DB_NAME} ON ${UserDatabase.DB_NAME}.id = ${EventDatabase.DB_NAME}.user_id " +
                "WHERE ${UserDatabase.DB_NAME}.id = :userId " +
                "AND ${EventDatabase.DB_NAME}.time BETWEEN :start_epoch_time AND :end_epoch_time " +
                "ORDER BY ${EventDatabase.DB_NAME}.time DESC"
    )
    fun selectEventsOfUserByFlow(
        userId: Long,
        start_epoch_time: Long,
        end_epoch_time: Long
    ): Flow<List<EventEntity>>

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

    @Query(
        "DELETE FROM ${EventDatabase.DB_NAME} " +
                "WHERE ${EventDatabase.DB_NAME}.user_id = :userId"
    )
    fun deleteAllEvent(userId: Long)
}