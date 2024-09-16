package com.idontwannafly.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.idontwannafly.taskmanager.ui.screens.Screen
import com.idontwannafly.taskmanager.ui.screens.list.ListScreen
import com.idontwannafly.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    TaskManagerTheme {
        NavHost(navController = navController, startDestination = Screen.List.route) {
            composable(Screen.List.route) { ListScreen() }
            composable(Screen.Details.route) { ListScreen() }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContentPreview() {
    MainContent()
}