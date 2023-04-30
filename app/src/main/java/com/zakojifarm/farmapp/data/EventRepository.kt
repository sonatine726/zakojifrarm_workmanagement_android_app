package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getAllOfUser(user: UserEntity): List<EventEntity>

    fun getAllOfUser(user: UserEntity, limit: Int): List<EventEntity>

    fun getAllOfUserByFlow(user: UserEntity): Flow<List<EventEntity>>

    fun getAllOfUserByFlow(user: UserEntity, limit: Int): Flow<List<EventEntity>>

    fun getTodayAllOfUserByFlow(user: UserEntity): Flow<List<EventEntity>>

    fun add(user: UserEntity, event: EventEntity)

    fun update(event: EventEntity)

    fun delete(event: EventEntity)

    fun deleteAllOfUser(user: UserEntity)
}