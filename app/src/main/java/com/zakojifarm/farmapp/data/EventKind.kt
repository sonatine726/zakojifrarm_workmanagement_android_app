package com.zakojifarm.farmapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EventKind(val id: Int, val workStatus: WorkStatus) : Parcelable {
    START_WORK(1, WorkStatus.WORKING),
    END_WORK(2, WorkStatus.OFF_DUTY),
    START_BREAK(3, WorkStatus.BREAK),
    END_BREAK(4, WorkStatus.WORKING),
    CHANGE_WORK(5, WorkStatus.WORKING);

    companion object {
        private val VALUES = values()
        fun fromId(id: Int) = VALUES.firstOrNull { it.id == id }
    }
}