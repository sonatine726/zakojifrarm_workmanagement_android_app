package com.zakojifarm.farmapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zakojifarm.farmapp.R

@Composable
fun CommonNavDrawer(onClickHelp: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.main_icon),
                contentDescription = "Image",
                tint = Color.Unspecified,
                modifier = Modifier.size(width = 30.dp, height = 30.dp)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default
                )
            )
        }

        Spacer(Modifier.height(24.dp))
        ClickableText(
            text = AnnotatedString(stringResource(R.string.menu_help)),
            style = MaterialTheme.typography.labelLarge,
            onClick = onClickHelp
        )
    }
}