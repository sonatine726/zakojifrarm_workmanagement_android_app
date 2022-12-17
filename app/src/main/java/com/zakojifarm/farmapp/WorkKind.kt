package com.zakojifarm.farmapp

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WorkKind(@StringRes private val stringId: Int) : Parcelable {
    MOWING(R.string.work_kind_mowing),
    TRACTOR(R.string.work_kind_tractor),
    OTHERS(R.string.work_kind_others);

    override fun toString(): String {
        return MainApplication.instance.resources.getString(stringId)
    }
}