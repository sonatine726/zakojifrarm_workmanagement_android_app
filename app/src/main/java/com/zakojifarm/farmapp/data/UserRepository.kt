package com.zakojifarm.farmapp.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAll(): List<UserEntity>

    fun getAllByFlow(): Flow<List<UserEntity?>>

    suspend fun add(user: UserEntity)

    suspend fun update(user: UserEntity)

    suspend fun delete(user: UserEntity)
}