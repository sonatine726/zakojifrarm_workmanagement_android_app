package com.zakojifarm.farmapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EventKind(val id: Int) : Parcelable {
    START_WORK(1),
    END_WORK(2),
    START_BREAK(3),
    END_BREAK(4),
    CHANGE_WORK(5);

    companion object {
        private val VALUES = values()
        fun fromId(id: Int) = VALUES.firstOrNull { it.id == id }
    }
}