package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow

class EventRepositoryImpl(private val eventDao: EventDao) : EventRepository {
    override fun get(id: Int): Flow<EventEntity?> = eventDao.get(id)

    override fun getAll(): Flow<List<EventEntity?>> = eventDao.selectAll()

    override fun getAllOfUser(user: UserEntity): Flow<List<EventEntity?>> =
        eventDao.selectAllOfSpecificUser(user.id)

    override fun getAllOfUser(user: UserEntity, limit: Int): Flow<List<EventEntity?>> =
        eventDao.selectAllOfSpecificUser(user.id, limit)

    override suspend fun add(user: UserEntity, event: EventEntity) {
        event.userId = user.id
        eventDao.insert(event)
    }

    override suspend fun update(event: EventEntity) = eventDao.update(event)

    override suspend fun delete(event: EventEntity) = eventDao.delete(event)
}