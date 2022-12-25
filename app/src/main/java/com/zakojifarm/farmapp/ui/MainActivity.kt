package com.zakojifarm.farmapp.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
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
}