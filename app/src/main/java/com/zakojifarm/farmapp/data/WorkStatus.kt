package com.zakojifarm.farmapp.data

import android.os.Parcelable
import androidx.annotation.StringRes
import com.zakojifarm.farmapp.MainApplication
import com.zakojifarm.farmapp.R
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