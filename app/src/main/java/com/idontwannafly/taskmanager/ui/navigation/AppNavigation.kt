package com.idontwannafly.taskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Navigation.Routes.LIST
    ) {
        composable(
            route = Navigation.Routes.LIST
        ) {
            ListScreenDestination(navController)
        }

        composable(
            route = Navigation.Routes.DETAILS,
            arguments = listOf(
                navArgument(name = Navigation.Args.TASK_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val taskId = requireNotNull(backStackEntry.arguments?.getString(Navigation.Args.TASK_ID)) { "Task id required" }
            DetailsScreenDestination(taskId, navController)
        }
    }
}


object Navigation {

    object Args {
        const val TASK_ID = "taskId"
    }

    object Routes {
        const val LIST = "list"
        const val DETAILS = "details/{${Args.TASK_ID}}"
    }
}

fun NavController.navigateToDetails(taskId: String) {
    navigate(Navigation.Routes.DETAILS.replace("{${Navigation.Args.TASK_ID}}", taskId))
}