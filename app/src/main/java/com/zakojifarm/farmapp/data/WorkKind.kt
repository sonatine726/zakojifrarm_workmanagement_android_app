package com.zakojifarm.farmapp.data

import android.os.Parcelable
import androidx.annotation.StringRes
import com.zakojifarm.farmapp.MainApplication
import com.zakojifarm.farmapp.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WorkKind(val id: Int, @StringRes private val stringId: Int) : Parcelable {
    MOWING(1, R.string.work_kind_mowing),
    TRACTOR(2, R.string.work_kind_tractor),
    OTHERS(3, R.string.work_kind_others);

    companion object {
        private val VALUES = values()
        fun fromId(id: Int) = VALUES.firstOrNull { it.id == id }
        fun fromString(str: String) = VALUES.firstOrNull { it.toString() == str }
    }

    override fun toString(): String {
        return MainApplication.instance.resources.getString(stringId)
    }
}