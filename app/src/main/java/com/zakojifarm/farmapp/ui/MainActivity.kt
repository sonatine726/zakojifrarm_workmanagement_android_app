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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.zakojifarm.farmapp.Constant
import com.zakojifarm.farmapp.MainApplication
import com.zakojifarm.farmapp.R
import com.zakojifarm.farmapp.data.WorkKind
import com.zakojifarm.farmapp.data.WorkStatus
import com.zakojifarm.farmapp.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val TIME_TIMER_SCHEDULED_PERIOD_MS = 1000L
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
                Scaffold(
                    containerColor = Color(
                        MainApplication.instance.resources.getColor(
                            R.color.main_screen_bg_color,
                            MainApplication.instance.theme
                        )
                    )
                ) {
                    MainScreen()
                }
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

    @Composable
    fun MainScreen() {
        val userName = viewModel.userName.collectAsState()
        val workStatus = viewModel.workStatus.collectAsState()
        val workKind = viewModel.workKind.collectAsState()

        Column(modifier = Modifier.padding(16.dp)) {
            CurrentTimeText()
            Spacer(Modifier.size(1.dp))
            UserName(name = userName.value, onNameChange = { viewModel.setUserName(it) })
            Spacer(Modifier.size(1.dp))
            WorkingStatus(workStatus.value, workKind.value)
            Spacer(Modifier.size(1.dp))
            Button(
                onClick = { /* Do something */ },
            ) {
                Text("勤務開始")
            }
            Spacer(Modifier.size(1.dp))
            Button(
                onClick = { /* Do something */ },
            ) {
                Text("休憩")
            }
            Spacer(Modifier.size(1.dp))
            Button(
                onClick = { /* Do something */ },
            ) {
                Text("データアップロード")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserName(name: String, onNameChange: (String) -> Unit) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (name.isNotEmpty()) {
                Text(
                    text = "Hello, $name",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Name") }
            )
        }
    }

    @Composable
    fun CurrentTimeText() {
        var currentMs by remember { mutableStateOf(System.currentTimeMillis()) }
        var timeTimer: Timer? = null

        Text(
            text = currentTimeStr(currentMs),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        LaunchedEffect(true) {
            timeTimer?.run {
                cancel()
                timeTimer = null
            }

            timeTimer = Timer().apply {
                scheduleAtFixedRate(
                    TIME_TIMER_SCHEDULED_PERIOD_MS,
                    TIME_TIMER_SCHEDULED_PERIOD_MS
                ) {
                    currentMs = System.currentTimeMillis()
                }
            }
        }
    }

    @Composable
    fun WorkingStatus(status: WorkStatus, kind: WorkKind) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.working_status_title),
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
            )
            Spacer(Modifier.size(1.dp))
            Text(
                text = "$status : $kind",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 24.sp
            )
        }
    }

    private fun currentTimeStr(timeMs: Long): String {
        val zonedDt = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(timeMs),
            ZoneId.of("Asia/Tokyo")
        )

        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        return df.format(zonedDt)
    }
}