package com.zakojifarm.farmapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String) {

    sealed class MainScreens(
        route: String,
        title: String,
        val icon: ImageVector
    ) : Screens(
        route,
        title
    ) {
        object Home : MainScreens("home", "Home", Icons.Filled.Home)
        object SignUp : MainScreens("sign_up", "Sign Up", Icons.Filled.Login)
        object Help : MainScreens("help", "Help", Icons.Filled.Help)
    }
}