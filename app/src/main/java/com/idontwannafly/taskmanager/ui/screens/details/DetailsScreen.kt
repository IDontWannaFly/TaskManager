package com.idontwannafly.taskmanager.ui.screens.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails
import com.idontwannafly.taskmanager.ui.screens.common.Header
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailsScreen(
    taskId: Long,
    viewModel: DetailsViewModel = koinViewModel(parameters = { parametersOf(taskId) })
) =
    Surface(modifier = Modifier.fillMaxSize()) {
        val details by viewModel.detailsFlow.collectAsState()
        DetailsScreenContent(details)
    }

@Composable
fun DetailsScreenContent(details: TaskDetails) {

}

@Preview
@Composable
fun DetailsScreenPreview() = Surface(Modifier.fillMaxSize()) {
    DetailsScreenContent(TaskDetails.empty())
}