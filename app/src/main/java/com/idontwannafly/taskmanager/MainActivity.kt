package com.idontwannafly.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.idontwannafly.taskmanager.ui.navigation.AppNavigation
import com.idontwannafly.taskmanager.ui.screens.Screen
import com.idontwannafly.taskmanager.ui.screens.details.ARGUMENT_TASK_ID
import com.idontwannafly.taskmanager.ui.screens.details.DetailsScreen
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

val LocalNavController = staticCompositionLocalOf<NavController?> { null }

@Composable
fun MainContent() {
    TaskManagerTheme {
        Surface {
            AppNavigation()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContentPreview() {
    MainContent()
}