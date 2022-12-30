package com.zakojifarm.farmapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation(viewModel: WorkStatusViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.MainScreens.Home.route
    ) {
        val onInitializeUserName = { name: String ->
            viewModel.initializeUser(name)
            navController.navigate(Screens.MainScreens.Home.route)
        }

        composable(Screens.MainScreens.Home.route) {
            MainScreen(
                viewModel,
                navController
            )
        }
        composable(Screens.MainScreens.SignUp.route) {
            SignUpScreen(navController, onInitializeUserName = { onInitializeUserName(it) })
        }
        composable(Screens.MainScreens.Help.route) {
            HelpScreen(navController)
        }
    }
}