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
import java.util.*


class GoogleDriveUtils {
    companion object {
        private val TAG = GoogleDriveUtils::class.java.simpleName

        private val service by lazy {
            val credentials =
                GoogleCredentials.fromStream(MainApplication.instance.assets.open("zakojifarmapp-0d33671abfa7.json"))
                    .createScoped("https://www.googleapis.com/auth/drive")

            val requestInitializer: HttpRequestInitializer = HttpCredentialsAdapter(
                credentials
            )

            Drive.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer
            )
                .setApplicationName("ZakojiFarmApp")
                .build()
        }

        fun upload(file: java.io.File): com.google.api.services.drive.model.File {
            Log.v(TAG, "upload(),${file.name}")

            val fileMetadata = com.google.api.services.drive.model.File().apply {
                name = file.name
                parents = Collections.singletonList("1K6FDlRYgsGVMJFF8uJoa_ErIckxko2CX")
            }

            val mediaContent = FileContent("text/plain", file)

            try {
                return service.files().create(fileMetadata, mediaContent).setFields("id").execute()
            } catch (e: GoogleJsonResponseException) {
                Log.e(TAG, "upload exception,${e.details}")
                throw e
            }
        }

        val fileList: List<com.google.api.services.drive.model.File>
            get() {
                val files = mutableListOf<com.google.api.services.drive.model.File>()

                var pageToken: String? = null
                do {
                    val result = service.files().list()
                        .setQ("name contains 'zakojifarm_events' and '1K6FDlRYgsGVMJFF8uJoa_ErIckxko2CX' in parents")
                        .setSpaces("drive")
                        .setPageToken(pageToken)
                        .execute()

                    files.addAll(result.files)
                    pageToken = result.nextPageToken
                } while (pageToken != null)

                return files
            }

        fun delete(file: com.google.api.services.drive.model.File) {
            Log.v(TAG, "delete(),${file.name}")

            try {
                service.files().delete(file.id).execute()
            } catch (e: GoogleJsonResponseException) {
                Log.e(TAG, "delete exception,${e.details}")
                throw e
            }
        }
    }
}