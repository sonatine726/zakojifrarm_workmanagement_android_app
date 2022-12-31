package com.zakojifarm.farmapp.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zakojifarm.farmapp.MainApplication
import com.zakojifarm.farmapp.R
import kotlinx.coroutines.launch

private const val TAG = "WindowTemplate"

@Composable
@ExperimentalMaterial3Api
fun WindowTemplate(
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    val crScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val onClickHome = { _: Int ->
        Log.v(TAG, "onClickHome")
        navController.navigate(Screens.MainScreens.Home.route) {
            launchSingleTop = true
        }
    }

    val onClickHelp = { _: Int ->
        Log.v(TAG, "onClickHelp")
        navController.navigate(Screens.MainScreens.Help.route) {
            launchSingleTop = true
        }
    }

    return ModalNavigationDrawer(
        drawerContent = {
            Column(
                modifier = Modifier
                    .requiredWidth(250.dp)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.background,
                        RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
            ) {
                CommonNavDrawer(
                    onClickHome = {
                        crScope.launch { drawerState.close() }
                        onClickHome(it)
                    },
                    onClickHelp = {
                        crScope.launch { drawerState.close() }
                        onClickHelp(it)
                    },
                    modifier = modifier
                )
            }
        },
        modifier,
        drawerState,
        gesturesEnabled,
        scrimColor
    ) {
        Scaffold(
            snackbarHost = { snackbarHostState },
            containerColor = Color(
                MainApplication.instance.resources.getColor(
                    R.color.main_screen_bg_color,
                    MainApplication.instance.theme
                )
            ),
            topBar = {
                CommonTopAppBar(onClickNavigationIcon = { crScope.launch { drawerState.open() } })
            },
            content = content
        )
    }
}
