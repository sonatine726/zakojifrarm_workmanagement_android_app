package com.zakojifarm.farmapp.hilt

import com.zakojifarm.farmapp.data.EventRepository
import com.zakojifarm.farmapp.data.EventRepositoryImpl
import com.zakojifarm.farmapp.data.UserRepository
import com.zakojifarm.farmapp.data.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository

}