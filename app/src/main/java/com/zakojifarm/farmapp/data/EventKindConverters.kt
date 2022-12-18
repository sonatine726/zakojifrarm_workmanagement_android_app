package com.zakojifarm.farmapp.data

import androidx.room.TypeConverter

class EventKindConverters {
    @TypeConverter
    fun toEventKind(value: Int?): EventKind? =
        value?.let { EventKind.fromId(it) }

    @TypeConverter
    fun fromEventKind(kind: EventKind?): Int? = kind?.id
}