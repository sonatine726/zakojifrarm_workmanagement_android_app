package com.zakojifarm.farmapp.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import com.zakojifarm.farmapp.Constant
import com.zakojifarm.farmapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var canStart = false

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.setIsRequestedLocationUpdates(true)
        } else {
            val toast = Toast.makeText(
                this,
                "Error", Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    private val viewModel: WorkStatusViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().run {
            setOnExitAnimationListener { viewProvider ->
                val splashScreen = viewProvider.view
                ObjectAnimator.ofFloat(
                    splashScreen,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreen.height.toFloat()
                ).apply {
                    interpolator = LinearInterpolator()
                    duration = 1000L
                    startDelay = Constant.SPLASH_SCREEN_DURATION

                    doOnEnd { viewProvider.remove() }
                    start()
                }
            }
        }

        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            viewModel.setIsRequestedLocationUpdates(true)
        }

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override
            fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.v(TAG, "onLocationResult.$location")
                    viewModel.setCurrentLocation(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    )
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            AppTheme {
                Navigation(viewModel = viewModel)
            }
        }

        lifecycleScope.launch {
            delay(Constant.SPLASH_SCREEN_DURATION)
            canStart = true
        }

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    Log.v(TAG, "PreDraw.$canStart")
                    if (canStart) content.viewTreeObserver.removeOnPreDrawListener(this)
                    return canStart
                }
            }
        )
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isRequestedLocationUpdates.value) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}