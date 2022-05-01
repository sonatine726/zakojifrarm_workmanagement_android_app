package com.zakojifarm.farmapp.utils

import com.zakojifarm.farmapp.BuildConfig

class Utils {
    companion object {
        val isDebug get() = BuildConfig.DEBUG
    }
}