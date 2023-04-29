package com.zakojifarm.farmapp.domain

import com.zakojifarm.farmapp.MainApplication
import com.zakojifarm.farmapp.data.UserEntity
import com.zakojifarm.farmapp.utils.GoogleDriveUtils
import java.io.File
import java.io.FileWriter
import java.io.IOException

class EventList(user: UserEntity) {
    fun uploadEventListToGoogleDrive(): com.google.api.services.drive.model.File {
        val file = File(MainApplication.instance.cacheDir, "test2.txt")
        try {
            FileWriter(file).use { writer -> writer.write("testes") }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return GoogleDriveUtils.upload(file)
    }
}