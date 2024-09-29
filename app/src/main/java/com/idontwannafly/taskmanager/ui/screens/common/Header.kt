package com.idontwannafly.taskmanager.ui.screens.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    modifier: Modifier = Modifier,
    text: String = "",
    fontSize: TextUnit = 20.sp
) {
    Text(
        modifier = Modifier.fillMaxWidth()
            .then(modifier),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = fontSize,
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
    )
}