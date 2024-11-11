package com.idontwannafly.taskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.idontwannafly.taskmanager.ui.screens.list.ListContract
import com.idontwannafly.taskmanager.ui.screens.list.ListScreen
import com.idontwannafly.taskmanager.ui.screens.list.ListViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListScreenDestination(navController: NavController) {
    val viewModel = koinViewModel<ListViewModel>()
    val state = viewModel.viewState.collectAsStateWithLifecycle()
    ListScreen(
        state = state.value,
        effectFlow = viewModel.effect,
        onEventSent = { viewModel.postEvent(it) },
        onNavigationRequested = { effect ->
            when (effect) {
                is ListContract.Effect.Navigation.ToDetails -> navController.navigateToDetails(effect.taskId)
            }
        }
    )
}