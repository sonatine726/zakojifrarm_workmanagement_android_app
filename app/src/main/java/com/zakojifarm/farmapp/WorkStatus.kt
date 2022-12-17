package com.zakojifarm.farmapp

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WorkStatus(@StringRes private val stringId: Int) : Parcelable {
    OFF_DUTY(R.string.work_status_off_duty),
    WORKING(R.string.work_status_working),
    BREAK(R.string.work_status_break);

    override fun toString(): String {
        return MainApplication.instance.resources.getString(stringId)
    }
}