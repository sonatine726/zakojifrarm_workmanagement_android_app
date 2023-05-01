package com.zakojifarm.farmapp.utils

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

class DateUtils {
    companion object {
        private val TAG = DateUtils::class.java.simpleName

        fun epochMilliToSystemZonedDateTime(epochMilli: Long): ZonedDateTime {
            return ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(epochMilli),
                ZoneOffset.systemDefault()
            )
        }
    }
}