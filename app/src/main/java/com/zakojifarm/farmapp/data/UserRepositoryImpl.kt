package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun get(id: Int): Flow<UserEntity?> = userDao.get(id)

    override fun getAll(): Flow<List<UserEntity?>> = userDao.selectAll()

    override suspend fun add(user: UserEntity) = userDao.insert(user)

    override suspend fun update(user: UserEntity) = userDao.update(user)

    override suspend fun delete(user: UserEntity) = userDao.delete(user)
}