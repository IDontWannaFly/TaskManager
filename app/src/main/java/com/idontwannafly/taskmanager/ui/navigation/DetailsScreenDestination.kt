package com.idontwannafly.taskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.idontwannafly.taskmanager.ui.screens.details.DetailsContract
import com.idontwannafly.taskmanager.ui.screens.details.DetailsScreen
import com.idontwannafly.taskmanager.ui.screens.details.DetailsViewModel
import com.idontwannafly.taskmanager.ui.screens.list.ListContract
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailsScreenDestination(taskId: String, navController: NavController) {
    val viewModel = koinViewModel<DetailsViewModel>(parameters = { parametersOf(taskId) })
    val state = viewModel.viewState.collectAsStateWithLifecycle()
    DetailsScreen(
        state = state.value,
        effectFlow = viewModel.effect,
        onEventSent = { viewModel.postEvent(it) },
        onNavigationRequested = { effect ->
            when (effect) {
                DetailsContract.Effect.Navigation.ToList -> navController.popBackStack()
            }
        }
    )
}