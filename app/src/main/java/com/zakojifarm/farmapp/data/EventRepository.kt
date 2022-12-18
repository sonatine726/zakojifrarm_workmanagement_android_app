package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun get(id: Int): Flow<EventEntity?>

    fun getAll(): Flow<List<EventEntity?>>

    fun getAllOfUser(user: UserEntity): Flow<List<EventEntity?>>

    fun getAllOfUser(user: UserEntity, limit: Int): Flow<List<EventEntity?>>

    suspend fun add(user: UserEntity, event: EventEntity)

    suspend fun update(event: EventEntity)

    suspend fun delete(event: EventEntity)
}