package com.zakojifarm.farmapp.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val userDao: UserDao) : EventRepository {
    companion object {
        private const val ONE_DAY_SEC = 24 * 60 * 60
    }

    override fun getAllOfUser(user: UserEntity): List<EventEntity> =
        userDao.selectEventsOfUser(user.id)

    override fun getAllOfUser(user: UserEntity, limit: Int): List<EventEntity> =
        userDao.selectEventsOfUser(user.id, limit)

    override fun getAllOfUserByFlow(user: UserEntity): Flow<List<EventEntity>> =
        userDao.selectEventsOfUserByFlow(user.id)

    override fun getAllOfUserByFlow(user: UserEntity, limit: Int): Flow<List<EventEntity>> =
        userDao.selectEventsOfUserByFlow(user.id, limit)

    override fun getTodayAllOfUserByFlow(user: UserEntity): Flow<List<EventEntity>> {
        val startAndEndUnixEpoch = startAndEndUnixEpochOfToday()
        Log.v("TesTes", "TesTes.1.$startAndEndUnixEpoch")
        return userDao.selectEventsOfUserByFlow(
            user.id,
            startAndEndUnixEpoch.start,
            startAndEndUnixEpoch.end
        )
    }

    private data class StartAndEndUnixEpochOfDay(val start: Long, val end: Long)

    private fun startAndEndUnixEpochOfToday(): StartAndEndUnixEpochOfDay {
        val todayCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val start = todayCalendar.timeInMillis / 1000
        val end = start + (ONE_DAY_SEC - 1)

        return StartAndEndUnixEpochOfDay(start, end)
    }

    override suspend fun add(user: UserEntity, event: EventEntity) {
        event.userId = user.id
        userDao.insertEvent(event)
    }

    override suspend fun update(event: EventEntity) = userDao.updateEvent(event)

    override fun delete(event: EventEntity) = userDao.deleteEvent(event)

    override suspend fun deleteAllOfUser(user: UserEntity) = userDao.deleteAllEvent(user.id)
}