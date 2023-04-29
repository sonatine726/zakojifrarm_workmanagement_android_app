package com.zakojifarm.farmapp.utils

import android.util.Log
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.FileContent
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.zakojifarm.farmapp.MainApplication
import java.io.File
import java.util.*


class GoogleDriveUtils {
    companion object {
        private val TAG = GoogleDriveUtils::class.java.simpleName

        fun upload(file: File): com.google.api.services.drive.model.File {
            Log.v(TAG, "uploadBarcodeFileToDrive(),${file.name}")
            val credentials =
                GoogleCredentials.fromStream(MainApplication.instance.assets.open("zakojifarmapp-0d33671abfa7.json"))
                    .createScoped("https://www.googleapis.com/auth/drive")

            val requestInitializer: HttpRequestInitializer = HttpCredentialsAdapter(
                credentials
            )

            val service = Drive.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer
            )
                .setApplicationName("ZakojiFarmApp")
                .build()

            // Upload file photo.jpg on drive.
            val fileMetadata = com.google.api.services.drive.model.File().apply {
                name = file.name
                parents = Collections.singletonList("1K6FDlRYgsGVMJFF8uJoa_ErIckxko2CX")
            }

            val mediaContent = FileContent("text/plain", file)

            try {
                return service.files().create(fileMetadata, mediaContent).setFields("id").execute()
            } catch (e: GoogleJsonResponseException) {
                Log.e(TAG, "Unable to upload file,${e.details}")
                throw e
            }
        }
    }
}