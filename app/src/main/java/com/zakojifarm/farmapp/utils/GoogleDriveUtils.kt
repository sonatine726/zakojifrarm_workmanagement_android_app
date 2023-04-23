package com.zakojifarm.farmapp.utils

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.zakojifarm.farmapp.MainApplication


class GoogleDriveUtils {
    companion object {
        private val TAG = GoogleDriveUtils::class.java.simpleName

        fun uploadBarcodeFileToDrive() {
            Log.v(TAG, "uploadBarcodeFileToDrive")
            val credentials =
                GoogleCredentials.fromStream(MainApplication.instance.assets.open("zakojifarmapp-1b66497aedf8.json"))

//            credentials.refreshIfExpired()
//            Log.v(TAG, "TesTes.$credentials\n${credentials.accessToken}\n")


            //        credentials.refreshIfExpired()
//        val token = credentials.accessToken

////        val resourceAsStream: InputStream = AuthTest::class.java.getClassLoader()
//            .getResourceAsStream("Google-Play-Android-Developer.json")
//
////        val credential = GoogleCredential.fromStream(resourceAsStream)
//        val credential = GoogleCredentials.fromStream(resourceAsStream)
//
//        // Get service account secret
//        val inputStream = MainActivity::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
//            ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
//
//        // Convert inputStream to file
//        val clientSecret: File = File(getFilesDir() + File.separator + "credentials.p12")
//        val outputStream: OutputStream = FileOutputStream(clientSecret)
//        IOUtils.copy(inputStream, outputStream)
//        if (!clientSecret.exists()) throw FileNotFoundException("Credentials (credentials.p12) not created from: $CREDENTIALS_FILE_PATH")
//
//        // Http transport creation
//        val httpTransport: HttpTransport = AndroidHttp.newCompatibleTransport()
//        // Instance of the JSON factory
//        val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
//        // Instance of the scopes required
//        val scopes: MutableList<String> = ArrayList()
//        scopes.add(DriveScopes.DRIVE)
//        // Build Google credential
//        val credential = GoogleCredential.Builder()
//            .setTransport(httpTransport)
//            .setJsonFactory(jsonFactory)
//            .setServiceAccountId(SERVICE_ACCOUNT_PROVIDER)
//            .setServiceAccountScopes(scopes)
//            .setServiceAccountPrivateKeyFromP12File(clientSecret)
//            .setServiceAccountUser(SERVICE_ACCOUNT_USER)
//            .build()
//        // Build Drive service
//        val service: Drive = Builder(httpTransport, jsonFactory, credential)
//            .setApplicationName(APPLICATION_NAME)
//            .build()
//
//        // Parents
//        val parents: MutableList<String> = ArrayList()
//        parents.add(TARGET_FOLDER_ID)
//        // Setup file
//        val timeStamp: String =
//            SimpleDateFormat("yyyyMMddHHmmss", Locale.forLanguageTag("hu-HU")).format(
//                Date()
//            )
//        val fileMetadata = File()
//        fileMetadata.setName("scan_$timeStamp.txt")
//        fileMetadata.setParents(parents)
//        val filePath: File = File(getFilesDir() + File.separator + BARCODE_CONTAINER_TEMP_FILE)
//        val mediaContent = FileContent("text/plain", filePath)
//        // Upload file to google drive
//        service.files().create(fileMetadata, mediaContent)
//            .setFields("id")
//            .execute()
        }
    }
}