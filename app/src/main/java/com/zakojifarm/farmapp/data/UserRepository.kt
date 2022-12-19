package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun get(id: Int): Flow<UserEntity?>

    fun getAll(): Flow<List<UserEntity?>>

    suspend fun add(user: UserEntity)

    suspend fun update(user: UserEntity)

    suspend fun delete(user: UserEntity)
}