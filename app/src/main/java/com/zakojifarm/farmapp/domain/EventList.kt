package com.zakojifarm.farmapp.domain

import android.util.Log
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.zakojifarm.farmapp.MainApplication
import com.zakojifarm.farmapp.data.EventEntity
import com.zakojifarm.farmapp.data.EventRepository
import com.zakojifarm.farmapp.data.UserEntity
import com.zakojifarm.farmapp.utils.GoogleDriveUtils
import java.io.File
import java.lang.StringBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventList(private val user: UserEntity, private val eventRepository: EventRepository) {
    companion object {
        private val TAG = EventList::class.java.simpleName

        private val CSV_HEADER_ROW = listOf(
            "EventID",
            "Time",
            "Kind(ID)",
            "Kind(Name)",
            "WorkKind(ID)",
            "WorkKind(Name)",
            "User(ID)",
            "User(Name)"
        )

        private const val PREFIX_OF_EVENTS_FILE_NAME = "zakojifarm_events"
        private const val SUFFIX_TO_REPRESENT_FINAL_OF_EVENTS_FILE_NAME = "final"
        private const val EXTENSION_OF_EVENTS_FILE_NAME = "csv"

        private const val DAY_PERIOD_FROM_TODAY_TO_KEEP_OLD_EVENTS_IN_DB = 10L
    }

    fun uploadEventListsToGoogleDrive() {
        val eventsByDay = allEventsByDay(user)
        val gDriveFileList = GoogleDriveUtils.fileList
        val gDriveFileNameList = gDriveFileList.map { it.name }.distinct()
        val today = LocalDate.now()

        eventsByDay.forEach { (date, events) ->
            val isFinalVersion = date < today
            val fileName = makeEventsFileName(date, isFinalVersion)
            val isFileExist = gDriveFileNameList.contains(fileName)

            if (isFinalVersion && isFileExist) {
                Log.v(TAG, "CSV file already exists in GDrive.$date,$fileName")
            } else {
                val tempFile = makeEventsFileInCacheDir(fileName, events, user)
                if (isFileExist) {
                    val existFiles = gDriveFileList.filter { it.name == fileName }
                    existFiles.forEach {
                        GoogleDriveUtils.delete(it)
                    }
                }
                GoogleDriveUtils.upload(tempFile)
            }
        }

        deleteOldEvents(eventsByDay)
    }

    private fun allEventsByDay(user: UserEntity): Map<LocalDate, List<EventEntity>> {
        val resultMap = mutableMapOf<LocalDate, MutableList<EventEntity>>()

        val allEvents = eventRepository.getAllOfUser(user)
        allEvents.forEach { event ->
            val date = event.time.toLocalDate()

            val dateEvents = resultMap.getOrPut(date) { mutableListOf() }
            dateEvents.add(event)
        }

        return resultMap
    }

    private fun makeEventsFileInCacheDir(
        fileName: String,
        events: List<EventEntity>,
        user: UserEntity
    ): File {
        val fileInCache = File(
            MainApplication.instance.cacheDir, fileName
        )

        csvWriter().open(fileInCache) {
            writeRow(CSV_HEADER_ROW)

            events.forEach {
                val csvRow = makeEventRowOfCSV(it, user)
                writeRow(csvRow)
            }
        }

        return fileInCache
    }

    private fun makeEventRowOfCSV(event: EventEntity, user: UserEntity): List<String> {
        val resultList = mutableListOf<String>()
        resultList.add(event.id.toString())
        resultList.add(event.time.toString())
        resultList.add(event.kind.id.toString())
        resultList.add(event.kind.workStatus.toString())
        resultList.add(event.workKind.id.toString())
        resultList.add(event.workKind.toString())
        resultList.add(user.id.toString())
        resultList.add(user.name)

        return resultList
    }

    private fun makeEventsFileName(date: LocalDate, isFinalFile: Boolean): String {
        val fileNameBuilder = StringBuilder()
        fileNameBuilder.append(
            "${PREFIX_OF_EVENTS_FILE_NAME}_${
                date.format(
                    DateTimeFormatter.ofPattern(
                        "yyyyMMdd"
                    )
                )
            }"
        )
        if (isFinalFile) {
            fileNameBuilder.append("_$SUFFIX_TO_REPRESENT_FINAL_OF_EVENTS_FILE_NAME")
        }
        fileNameBuilder.append(".$EXTENSION_OF_EVENTS_FILE_NAME")

        return fileNameBuilder.toString()
    }

    private fun deleteOldEvents(eventsByDay: Map<LocalDate, List<EventEntity>>) {
        val criteriaDay = LocalDate.now().minusDays(DAY_PERIOD_FROM_TODAY_TO_KEEP_OLD_EVENTS_IN_DB)
        eventsByDay.forEach { (date, events) ->
            if (date < criteriaDay) {
                Log.d(TAG, "Delete old events,$date,${events.size}")
                events.forEach {
                    eventRepository.delete(it)
                }
            }
        }
    }
}