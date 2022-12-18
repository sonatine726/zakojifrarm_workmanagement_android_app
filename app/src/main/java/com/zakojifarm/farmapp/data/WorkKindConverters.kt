package com.zakojifarm.farmapp.data

import androidx.room.TypeConverter

class WorkKindConverters {
    @TypeConverter
    fun toWorkKind(value: Int?): WorkKind? =
        value?.let { WorkKind.fromId(it) }

    @TypeConverter
    fun fromWorkKind(work: WorkKind?): Int? = work?.id
}