package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val userDao: UserDao) : EventRepository {
    override fun getAllOfUser(user: UserEntity): List<EventEntity> =
        userDao.selectEventsOfUser(user.id)

    override fun getAllOfUser(user: UserEntity, limit: Int): List<EventEntity> =
        userDao.selectEventsOfUser(user.id, limit)

    override fun getAllOfUserByFlow(user: UserEntity): Flow<List<EventEntity>> =
        userDao.selectEventsOfUserByFlow(user.id)

    override fun getAllOfUserByFlow(user: UserEntity, limit: Int): Flow<List<EventEntity>> =
        userDao.selectEventsOfUserByFlow(user.id, limit)

    override suspend fun add(user: UserEntity, event: EventEntity) {
        event.userId = user.id
        userDao.insertEvent(event)
    }

    override suspend fun update(event: EventEntity) = userDao.updateEvent(event)

    override suspend fun delete(event: EventEntity) = userDao.deleteEvent(event)
}