package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val userDao: UserDao) : EventRepository {

    override fun getAllOfUser(user: UserEntity): Flow<List<EventEntity>> =
        userDao.selectUserAndEvents().map { it[user] ?: emptyList() }
//        userDao.selectUserAndEvents().map { it[user] ?: emptyList() }

    override fun getAllOfUser(user: UserEntity, limit: Int): Flow<List<EventEntity>> =
        userDao.selectUserAndEvents().map { it[user] ?: emptyList() }

    override suspend fun add(user: UserEntity, event: EventEntity) {
        event.userId = user.id
        userDao.insertEvent(event)
    }

    override suspend fun update(event: EventEntity) = userDao.updateEvent(event)

    override suspend fun delete(event: EventEntity) = userDao.deleteEvent(event)
}