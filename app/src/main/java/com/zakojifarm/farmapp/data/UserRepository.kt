package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun get(id: Int): Flow<UserEntity?>

    fun getAll(): List<UserEntity>

    fun getAllByFlow(): Flow<List<UserEntity?>>

    fun getAllWithEvents(): Flow<Map<UserEntity, List<EventEntity>>>

    fun getAllEvents(): Flow<List<EventEntity>>

    fun getEvents(user: UserEntity): Flow<List<EventEntity>>

    suspend fun add(user: UserEntity)

    suspend fun update(user: UserEntity)

    suspend fun delete(user: UserEntity)
}