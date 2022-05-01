package com.zakojifarm.farmapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun restartApp() {
        startActivity(
            Intent(this, SplashScreenActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            }
        )
        finish()
    }
}