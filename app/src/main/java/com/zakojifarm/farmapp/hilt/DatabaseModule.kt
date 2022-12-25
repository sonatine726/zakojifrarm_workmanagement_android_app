package com.zakojifarm.farmapp.hilt

import android.content.Context
import androidx.room.Room
import com.zakojifarm.farmapp.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext context: Context
    ): UserDatabase {
        return Room.databaseBuilder(context, UserDatabase::class.java, UserDatabase.DB_FILE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: UserDatabase): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideEventDatabase(
        @ApplicationContext context: Context
    ): EventDatabase {
        return Room.databaseBuilder(context, EventDatabase::class.java, EventDatabase.DB_FILE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideEventDao(db: EventDatabase): EventDao {
        return db.eventDao()
    }
}