package com.zakojifarm.farmapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")

        setContent {
            Text("Hello world!")
        }
    }

//    private fun restartApp() {
//        startActivity(
//            Intent(this, SplashScreenActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//            }
//        )
//        finish()
//    }
}