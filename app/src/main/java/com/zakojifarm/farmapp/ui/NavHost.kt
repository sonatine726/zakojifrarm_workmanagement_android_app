package com.zakojifarm.farmapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation(viewModel: WorkStatusViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreens.Main.route
    ) {
        val onClickHelp = { _: Int ->
            navController.navigate(Screens.DrawerScreens.Help.route) {
                popUpTo("home")
            }
        }

        composable(Screens.HomeScreens.Main.route) {
            MainScreen(
                viewModel,
                onClickHelp = onClickHelp,
                modifier = modifier
            )
        }
        composable(Screens.DrawerScreens.Help.route) {
            HelpScreen(
                viewModel,
                onClickHelp = onClickHelp,
                modifier = modifier
            )
        }
    }
}