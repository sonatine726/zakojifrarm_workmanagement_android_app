package com.zakojifarm.farmapp.data

import androidx.room.TypeConverter
import java.time.*

class LocalDateTimeConverters {
    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? =
        value?.let {
            ZonedDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneOffset.systemDefault())
                .toLocalDateTime()
        }

    @TypeConverter
    fun fromLocalDateTime(time: LocalDateTime?): Long? =
        time?.atZone(ZoneOffset.systemDefault())?.toEpochSecond()
}