package com.zakojifarm.farmapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class SplashScreenActivity : AppCompatActivity() {
    companion object {
        private val TAG = SplashScreenActivity::class.java.simpleName
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { granted ->
            if (granted.values.all { it }) {
                goToMainActivity()
            } else {
                Toast.makeText(
                    this,
                    "ZakojiFarmApp cannot be used because any permission is denied",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (!checkPermission()) {
                    requestPermissionLauncher.launch(Constant.permissions)
                }
            },
            Constant.SPLASH_SCREEN_DURATION
        )
    }

    private fun checkPermission(): Boolean {
        Constant.permissions.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(it))
                    Log.e(TAG, "failed to get permission: $it")

                return false
            }
        }
        return true
    }

    private fun goToMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            }
        )
        finish()
    }
}