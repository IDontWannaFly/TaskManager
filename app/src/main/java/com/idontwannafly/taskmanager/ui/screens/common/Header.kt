package com.idontwannafly.taskmanager.ui.screens.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Header(
    modifier: Modifier = Modifier,
    text: String = ""
) {
    Text(
        modifier = Modifier.fillMaxWidth()
            .then(modifier),
        text = text,
        textAlign = TextAlign.Center
    )
}