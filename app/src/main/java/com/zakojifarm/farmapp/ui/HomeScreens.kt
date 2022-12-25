package com.zakojifarm.farmapp.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.zakojifarm.farmapp.MainApplication
import com.zakojifarm.farmapp.R
import com.zakojifarm.farmapp.data.WorkKind
import com.zakojifarm.farmapp.data.WorkStatus
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

private const val TIME_TIMER_SCHEDULED_PERIOD_MS = 1000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreens(modifier: Modifier = Modifier, viewModel: WorkStatusViewModel) {
    viewModel.setCurrentScreen(Screens.DrawerScreens.Home)

    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val crScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .requiredWidth(250.dp)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.background,
                        RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
            ) {}
            SideDrawer(modifier)
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
            containerColor = Color(
                MainApplication.instance.resources.getColor(
                    R.color.main_screen_bg_color,
                    MainApplication.instance.theme
                )
            ),
            topBar = {
                MainTopAppBar(onClickNavigationIcon = { crScope.launch { drawerState.open() } })
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                MainScreen(viewModel, onDataUploadButtonClicked = {
                    crScope.launch {
                        snackBarHostState.showSnackbar(
                            "Snackbar Test"
                        )
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(onClickNavigationIcon: () -> Unit) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
            ) {
                Text(text = stringResource(id = R.string.app_name))
                Icon(
                    painter = painterResource(R.drawable.main_icon),
                    contentDescription = "Image",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(width = 40.dp, height = 40.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = {
                Log.v("MainTopAppBar", "navigationIcon onClick")
                onClickNavigationIcon()
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: WorkStatusViewModel, onDataUploadButtonClicked: () -> Unit) {
    val userName = viewModel.userName.collectAsState()
    val workStatus = viewModel.workStatus.collectAsState()
    val workKind = viewModel.workKind.collectAsState()
    var currentWorkKind by remember {
        mutableStateOf(WorkKind.MOWING)
    }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog)
        CustomDialog(value = "", setShowDialog = {
            showDialog = it
        }) {
            Log.v("TesTes", "CustomDialog : $it")
        }

    Column(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 16.dp
        )
    ) {
        CurrentTimeText(TIME_TIMER_SCHEDULED_PERIOD_MS)
        Spacer(Modifier.size(1.dp))
        UserName(name = userName.value, onNameChange = { viewModel.setUserName(it) })
        Spacer(Modifier.size(1.dp))
        WorkingStatus(workStatus.value, workKind.value)
        Spacer(Modifier.size(1.dp))
        Button(
            onClick = {
                Log.v("TesTes", "Button.onClick.Duty Start")
            },
        ) {
            Text("勤務開始")
        }
        Spacer(Modifier.size(1.dp))
        Button(
            onClick = {
                Log.v("TesTes", "Button.onClick.Break")
                showDialog = true
            },
        ) {
            Text("休憩")
        }
        Spacer(Modifier.size(1.dp))
        Button(
            onClick = {
                Log.v("TesTes", "Button.onClick.Data Upload")
                onDataUploadButtonClicked()
            },
        ) {
            Text("データアップロード")
        }
        WorkKindDropdownMenuBox(
            currentWorkKind
        ) {
            currentWorkKind = it
            Log.v("TesTes", "WorkKindDropdownMenu.Select.$it")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkKindDropdownMenuBox(currentKind: WorkKind, onValueChanged: (WorkKind) -> Unit) {
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
