package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {
    override fun get(id: Int): Flow<UserEntity?> = userDao.get(id)

    override fun getAll(): List<UserEntity> = userDao.selectAll()

    override fun getAllByFlow(): Flow<List<UserEntity?>> = userDao.selectAllByFlow()

    override fun getAllWithEvents(): Flow<Map<UserEntity, List<EventEntity>>> =
        userDao.selectUserAndEvents()

    override fun getAllEvents(): Flow<List<EventEntity>> {
        return userDao.selectUserAndEvents().map { it.values.flatten() }
    }

    override fun getEvents(user: UserEntity): Flow<List<EventEntity>> {
        return userDao.selectUserAndEvents().map { it[user] ?: emptyList() }
    }

    override suspend fun add(user: UserEntity) = userDao.insert(user)

    override suspend fun update(user: UserEntity) = userDao.update(user)

    override suspend fun delete(user: UserEntity) = userDao.delete(user)
}