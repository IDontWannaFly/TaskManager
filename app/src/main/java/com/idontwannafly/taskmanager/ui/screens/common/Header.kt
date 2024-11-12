@file:OptIn(ExperimentalMaterial3Api::class)

package com.idontwannafly.taskmanager.ui.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idontwannafly.taskmanager.ui.screens.details.DetailsContract

@Composable
fun Header(
    modifier: Modifier = Modifier,
    text: String = "",
    fontSize: TextUnit = 20.sp,
    onBackPressed: (() -> Unit)? = null
) = TopAppBar(
    navigationIcon = {
        if (onBackPressed == null) return@TopAppBar
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    onBackPressed()
                }
                .padding(15.dp)
                .width(24.dp)
                .height(24.dp)
        )
    },
    actions = {
        Box(
            Modifier
                .width(54.dp)
        ) {  }
    },
    title = {
        Text(
            modifier = Modifier.fillMaxWidth()
                .then(modifier),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = fontSize,
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
        )
    }
)