package com.zakojifarm.farmapp.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.zakojifarm.farmapp.R
import com.zakojifarm.farmapp.data.*
import com.zakojifarm.farmapp.domain.EventList
import com.zakojifarm.farmapp.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

private const val TIME_TIMER_SCHEDULED_PERIOD_MS = 1000L
private const val TAG = "MainScreen"

private const val WORK_KIND_TEXT_ALPHA_IN_NOT_WORKING_STATUS = 0.5f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: WorkStatusViewModel,
    navController: NavHostController
) {
    val crScope = rememberCoroutineScope()

    WindowTemplate(
        navController = navController
    ) { innerPadding, snackbarHostState ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            var showDialog by remember { mutableStateOf(false) }

            if (showDialog)
                CustomDialog(value = "", setShowDialog = {
                    showDialog = it
                }) {
                    Log.v(TAG, "CustomDialog : $it")
                }

            MainWindow(viewModel, navController, onDataUploadButtonClicked = { user ->
                crScope.launch {
                    snackbarHostState.showSnackbar(
                        "Start uploading events to GDrive"
                    )
                    launch(Dispatchers.IO) {
                        val eventList = EventList(user, viewModel.eventRepository)
                        try {
                            eventList.uploadEventListsToGoogleDrive()
                            Log.d(
                                TAG,
                                "Uploaded events to GDrive"
                            )
                            snackbarHostState.showSnackbar(
                                "Succeed in uploading events to GDrive"
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to upload events to GDrive.$e")
                            snackbarHostState.showSnackbar(
                                "Error!. Failed to uploading events to GDrive"
                            )
                        }
                    }
                }
            })
        }
    }
}

@Composable
fun MainWindow(
    viewModel: WorkStatusViewModel,
    navController: NavHostController,
    onDataUploadButtonClicked: (UserEntity) -> Unit
) {
    val isUserSignIn = viewModel.isUserSignIn.collectAsState()
    val user = viewModel.user.collectAsState()
    var currentWorkKind by remember {
        mutableStateOf(WorkKind.OTHERS)
    }
    var showDialog by remember { mutableStateOf(false) }

    val todayEvents = viewModel.todayEvents.collectAsState()
    currentWorkKind =
        if (todayEvents.value.isEmpty()) WorkKind.OTHERS else todayEvents.value[0].workKind
    val workStatus =
        if (todayEvents.value.isEmpty()) WorkStatus.OFF_DUTY else todayEvents.value[0].kind.workStatus

    LaunchedEffect(true) {
        Log.v(TAG, "MainWindow::LaunchedEffect")

        if (!isUserSignIn.value) {
            launch(Dispatchers.IO) {
                viewModel.checkUserSignIn()

                if (!isUserSignIn.value) {
                    withContext(Dispatchers.Main) {
                        navController.navigate(Screens.MainScreens.SignUp.route) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }

//        viewModel.addUser(UserEntity.create("Kubota", "Test"))
//        viewModel.addEvent(EventEntity.create(EventKind.START_WORK, WorkKind.OTHERS))
//        viewModel.updateEvents()
//        viewModel.deleteAllEvent()
    }

    if (showDialog)
        CustomDialog(value = "", setShowDialog = {
            showDialog = it
        }) {
            Log.v(TAG, "CustomDialog : $it")
        }

    if (isUserSignIn.value) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            CurrentTimeText(TIME_TIMER_SCHEDULED_PERIOD_MS)
            WorkerName(user.value)
            WorkingStatus(workStatus, currentWorkKind)
            StartOrEndWorkButton(workStatus) {
                val event =
                    if (workStatus != WorkStatus.OFF_DUTY) EventKind.END_WORK else EventKind.START_WORK
                viewModel.addEvent(EventEntity.create(event, currentWorkKind))
            }
            BreakButton(workStatus) {
                val event =
                    if (workStatus == WorkStatus.BREAK) EventKind.END_BREAK else EventKind.START_BREAK
                viewModel.addEvent(EventEntity.create(event, currentWorkKind))
            }
            WorkKindDropdownMenuBox(
                workStatus,
                currentWorkKind
            ) {
                Log.v(TAG, "WorkKindDropdownMenu.Select.$it")
                viewModel.addEvent(EventEntity.create(EventKind.CHANGE_WORK, it))
            }
            Spacer(Modifier.height(40.dp))
            WorkLocationMap(viewModel)
            Button(
                onClick = {
                    user.value?.let {
                        Log.v(TAG, "Button.onClick.Data Upload.$it")
                        onDataUploadButtonClicked(it)
                    }
                },
            ) {
                Text(stringResource(R.string.data_upload))
            }

//
//            if (todayEvents.value.isNotEmpty()) {
//                Column {
//                    todayEvents.value.forEachIndexed { index, event ->
//                        Text("$index. ${event.time},${event.kind},${event.workKind},${event.userId}")
//                    }
//                }
//            }
        }
    }
}

@Composable
fun CurrentTimeText(timeUpdateMs: Long) {
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
        Log.v(TAG, "CurrentTimeText::LaunchedEffect")
        timeTimer?.run {
            cancel()
            timeTimer = null
        }

        timeTimer = Timer().apply {
            scheduleAtFixedRate(
                timeUpdateMs,
                timeUpdateMs
            ) {
                currentMs = System.currentTimeMillis()
            }
        }
    }
}

@Composable
private fun WorkerName(userEntity: UserEntity?) {
    if (userEntity != null) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                text = stringResource(R.string.worker_name_title),
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = userEntity.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 30.sp
                )
                if (userEntity.explanation != null) {
                    Text(
                        text = userEntity.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WorkingStatus(workStatus: WorkStatus, workKind: WorkKind?) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Text(
            text = stringResource(R.string.working_status_title),
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = workStatus.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 30.sp,
                color = when (workStatus) {
                    WorkStatus.WORKING -> Color.Green
                    WorkStatus.BREAK -> Color.Magenta
                    else -> MaterialTheme.typography.bodyLarge.color
                }
            )
            if (workStatus != WorkStatus.OFF_DUTY && workKind != null) {
                val kindTextStyle = MaterialTheme.typography.bodySmall
                Text(
                    text = workKind.toString(),
                    style = kindTextStyle,
                    fontSize = 16.sp,
                    color = Color(
                        kindTextStyle.color.red,
                        kindTextStyle.color.green,
                        kindTextStyle.color.blue,
                        if (workStatus == WorkStatus.WORKING) 1f else WORK_KIND_TEXT_ALPHA_IN_NOT_WORKING_STATUS,
                        kindTextStyle.color.colorSpace
                    )
                )
            }
        }
    }
}

private fun currentTimeStr(timeMs: Long): String {
    return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        .format(DateUtils.epochMilliToSystemZonedDateTime(timeMs))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkKindDropdownMenuBox(
    workStatus: WorkStatus,
    currentKind: WorkKind,
    onValueChanged: (WorkKind) -> Unit
) {
    val options = WorkKind.values().map { it.toString() }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = currentKind.toString(),
            onValueChange = { },
            label = { Text("仕事内容") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            enabled = workStatus == WorkStatus.WORKING
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        expanded = false
                        WorkKind.fromString(selectionOption)?.let {
                            onValueChanged(it)
                        }
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
private fun StartOrEndWorkButton(workStatus: WorkStatus, onClicked: () -> Unit) {
    Button(
        onClick = onClicked
    ) {
        val titleStrId =
            if (workStatus == WorkStatus.WORKING) R.string.end_work else R.string.start_work
        Text(stringResource(titleStrId))
    }
}

@Composable
private fun BreakButton(workStatus: WorkStatus, onClicked: () -> Unit) {
    Button(
        onClick = onClicked,
        enabled = workStatus != WorkStatus.OFF_DUTY
    ) {
        val titleStrId =
            if (workStatus == WorkStatus.BREAK) R.string.return_from_break else R.string.break_work
        Text(stringResource(titleStrId))
    }
}

@Composable
private fun WorkLocationMap(viewModel: WorkStatusViewModel) {
    val isRequestedLocationUpdates = viewModel.isRequestedLocationUpdates.collectAsState()
    if (isRequestedLocationUpdates.value) {
        val currentLocation = viewModel.currentLatLng.collectAsState()

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                currentLocation.value, 16f
            )
        }


        LaunchedEffect(currentLocation) {
            snapshotFlow { currentLocation.value }
                .collect {
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(it, cameraPositionState.position.zoom)
                    Log.v(TAG, "TesTes.10")
                }
        }

        GoogleMap(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(
                    position = LatLng(
                        currentLocation.value.latitude,
                        currentLocation.value.longitude
                    )
                ),
                title = "Current Position",
                snippet = "Current Position"
            )
        }
    }
}