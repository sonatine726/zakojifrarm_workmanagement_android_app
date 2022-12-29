package com.zakojifarm.farmapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.zakojifarm.farmapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController, onInitializeUserName: (String) -> Unit) {
    var inputName by remember { mutableStateOf("")}

    WindowTemplate(navController = navController) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = inputName,
                onValueChange = { inputName = it },
                label = { Text(stringResource(R.string.signup_text_field_title)) }
            )
            Button(
                onClick = {
                    onInitializeUserName(inputName)
                },
            ) {
                Text(stringResource(R.string.signup_button_title))
            }
        }
    }
}