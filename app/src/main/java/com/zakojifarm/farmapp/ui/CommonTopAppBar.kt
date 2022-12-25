package com.zakojifarm.farmapp.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zakojifarm.farmapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(onClickNavigationIcon: () -> Unit) {
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