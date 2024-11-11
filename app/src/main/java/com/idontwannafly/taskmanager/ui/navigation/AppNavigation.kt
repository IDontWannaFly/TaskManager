package com.idontwannafly.taskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Navigation.Routes.LIST
    ) {

    }
}


object Navigation {

    object Args {
        const val TASK_ID = "taskId"
    }

    object Routes {
        const val LIST = "list"
        const val DETAILS = "details"
    }
}

fun NavController.navigateToDetails(taskId: String) {
    navigate("${Navigation.Routes.DETAILS}/$taskId")
}