package com.idontwannafly.taskmanager.ui.screens.details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails
import com.idontwannafly.taskmanager.ui.base.SIDE_EFFECTS_KEY
import com.idontwannafly.taskmanager.ui.screens.common.Header
import com.idontwannafly.taskmanager.ui.screens.list.ListContract
import com.idontwannafly.taskmanager.ui.views.TaskTextField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val TAG = "DetailsScreen"

@Composable
fun DetailsScreen(
    state: DetailsContract.State,
    effectFlow: Flow<DetailsContract.Effect>,
    onEventSent: (event: DetailsContract.Event) -> Unit,
    onNavigationRequested: (navigation: DetailsContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow.collect {
            when (it) {
                is DetailsContract.Effect.Navigation.ToList -> onNavigationRequested(it)
            }
        }
    }

    Scaffold(
        topBar = {
            Box {
                Header(
                    text = state.details.name,
                    onBackPressed = {
                        val navigation = DetailsContract.Effect.Navigation.ToList
                        onNavigationRequested(navigation)
                    }
                )
                if (!state.isLoading) return@Box
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }
    ) { pv ->
        DetailsScreenContent(
            modifier = Modifier.padding(pv),
            details = state.details,
            updateDetailsAction = { details ->
                val event = DetailsContract.Event.UpdateDetails(details)
                onEventSent(event)
            }
        )
    }
}

@Composable
fun DetailsScreenContent(
    modifier: Modifier,
    details: TaskDetails,
    updateDetailsAction: (details: TaskDetails) -> Unit
) = Column(
    modifier = Modifier
        .then(modifier)
        .padding(horizontal = 15.dp)
) {
    Log.d(TAG, "Details")
    val isUpdating = remember {
        mutableStateOf(false)
    }
    val description = remember(details.taskId) { mutableStateOf(details.description) }
    LaunchedEffect(key1 = details.taskId) {
        Log.d(TAG, "LaunchedEffect details: $details")
        snapshotFlow { description.value }.debounce(1000L).collect {
            Log.d(TAG, "Snapshot flow details: $details with description ${description.value}")
            isUpdating.value = true
            updateDetailsAction(
                details.copy(
                    description = it
                )
            )
        }
    }
    Text(
        modifier = Modifier.padding(bottom = 5.dp),
        text = stringResource(R.string.description),
        fontSize = 16.sp,
        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
    )
    TaskTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .background(Color.LightGray.copy(alpha = 0.1f))
            .padding(5.dp),
        value = description.value,
        onValueChange = { description.value = it },
        hint = stringResource(R.string.description_hint)
    )
}

@Preview
@Composable
fun DetailsScreenPreview() = Surface(Modifier.fillMaxSize()) {
    val state = DetailsContract.State(TaskDetails.mock(), false)
    DetailsScreen(
        state,
        flowOf(),
        {},
        {}
    )
}