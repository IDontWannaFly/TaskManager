package com.idontwannafly.taskmanager.ui.screens.list

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import com.idontwannafly.taskmanager.ui.base.SIDE_EFFECTS_KEY
import com.idontwannafly.taskmanager.ui.screens.common.Header
import com.idontwannafly.taskmanager.ui.screens.list.composables.TaskFiller
import com.idontwannafly.taskmanager.ui.screens.list.composables.TaskItem
import com.idontwannafly.taskmanager.ui.screens.list.composables.TasksList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlin.math.absoluteValue

private const val TAG = "ListScreen"

@Composable
fun ListScreen(
    state: ListContract.State,
    effectFlow: Flow<ListContract.Effect>,
    onEventSent: (event: ListContract.Event) -> Unit,
    onNavigationRequested: (navigation: ListContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow.collect {
            when (it) {
                is ListContract.Effect.Navigation.ToDetails -> onNavigationRequested(it)
            }
        }
    }
    Scaffold(
        topBar = {
            Header(
                text = stringResource(R.string.tasks_list)
            )
        }
    ) { pv ->
        when {
            state.tasks.isEmpty() -> EmptyListScreen(
                Modifier.padding(pv),
                onEventSent
            )

            else -> TasksList(
                Modifier.padding(pv),
                state.tasks,
                onEventSent
            )
        }
    }
}

@Composable
fun EmptyListScreen(
    modifier: Modifier,
    onEventSent: (event: ListContract.Event) -> Unit
) = Column(
    modifier = Modifier
        .then(modifier)
        .padding(horizontal = 15.dp)
) {
    TaskFiller { name ->
        val event = ListContract.Event.AddTask(parentId = null, name = name)
        onEventSent(event)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListScreenPreview() = Surface(Modifier.fillMaxSize()) {
    val items = listOf(
        Task(name = "Simple task", index = 0),
        Task(name = "Second task", index = 1)
    )
    ListScreen(
        ListContract.State(items),
        flowOf(),
        {},
        {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyScreenPreview() = Surface(Modifier.fillMaxSize()) {
    EmptyListScreen(
        Modifier
    ) { }
}