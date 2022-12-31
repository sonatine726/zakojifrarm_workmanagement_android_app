package com.zakojifarm.farmapp.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation(viewModel: WorkStatusViewModel) {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }

    NavHost(
        navController = navController,
        startDestination = Screens.MainScreens.Home.route
    ) {
        val onInitializeUserName = { name: String ->
            viewModel.initializeUser(name)
            navController.navigate(Screens.MainScreens.Home.route) {
                launchSingleTop = true
            }
        }

        composable(Screens.MainScreens.Home.route) {
            MainScreen(
                viewModel,
                navController,
                snackBarHostState
            )
        }
        composable(Screens.MainScreens.SignUp.route) {
            SignUpScreen(navController,
                snackBarHostState, onInitializeUserName = { onInitializeUserName(it) })
        }
        composable(Screens.MainScreens.Help.route) {
            HelpScreen(
                navController,
                snackBarHostState
            )
        }
    }
}