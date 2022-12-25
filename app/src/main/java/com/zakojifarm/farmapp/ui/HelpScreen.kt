package com.zakojifarm.farmapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zakojifarm.farmapp.BuildConfig
import com.zakojifarm.farmapp.MainApplication
import com.zakojifarm.farmapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    viewModel: WorkStatusViewModel,
    onClickHelp: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    viewModel.setCurrentScreen(Screens.DrawerScreens.Help)

    val snackBarHostState = remember { SnackbarHostState() }
    val crScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CommonNavDrawer(onClickHelp = onClickHelp, modifier = modifier)
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
                CommonTopAppBar(onClickNavigationIcon = { crScope.launch { drawerState.open() } })
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.menu_app_version_title),
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = BuildConfig.VERSION_NAME,
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp
                    )
                }
                Text(
                    text = stringResource(R.string.menu_app_copyright),
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp
                )
            }
        }
    }
}