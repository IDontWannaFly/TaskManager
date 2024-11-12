package com.idontwannafly.taskmanager.ui.screens.list

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import com.idontwannafly.taskmanager.ui.base.SIDE_EFFECTS_KEY
import com.idontwannafly.taskmanager.ui.screens.common.Header
import com.idontwannafly.taskmanager.ui.screens.list.item.TaskItem
import com.idontwannafly.taskmanager.ui.views.TaskTextField
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

            else -> TaskList(
                Modifier.padding(pv),
                state.tasks, onEventSent, onNavigationRequested
            )
        }
    }
}

@Composable
fun EmptyListScreen(
    modifier: Modifier,
    onEventSent: (event: ListContract.Event) -> Unit
) = Column(
    modifier = Modifier.then(modifier)
        .padding(horizontal = 15.dp)
) {
    TaskFiller{ name ->
        val event = ListContract.Event.AddTask(name)
        onEventSent(event)
    }
}

@Composable
fun TaskList(
    modifier: Modifier,
    items: List<Task>,
    onEventSent: (event: ListContract.Event) -> Unit,
    onNavigationRequested: (navigation: ListContract.Effect.Navigation) -> Unit
) {
    val listState = rememberLazyListState()
    val position = remember { mutableFloatStateOf(0f) }
    val draggedItem = remember { mutableIntStateOf(-1) }
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { listState.layoutInfo }
            .combine(snapshotFlow { position.floatValue }) { state, pos ->
                Log.d(TAG, "Position: $pos")
                if (pos == 0f) return@combine -1
                val nearestItem =
                    state.visibleItemsInfo.minByOrNull { (pos - (it.offset + it.size / 2f)).absoluteValue }
                return@combine nearestItem?.index ?: -1
            }
            .distinctUntilChanged()
            .collect { near ->
                Log.d(TAG, "Dragged item: ${draggedItem.intValue}; Near: $near")
                draggedItem.intValue = when {
                    near == -1 -> -1
                    draggedItem.intValue == -1 -> near
                    else -> near.also {
                        val event = ListContract.Event.MoveItems(draggedItem.intValue, near)
                        onEventSent(event)
                    }
                }
            }
    }
    val indexWithOffset by remember {
        derivedStateOf {
            Log.d(TAG, "Dragged item: ${draggedItem.intValue}")
            if (draggedItem.intValue == -1) return@derivedStateOf null
            val item =
                listState.layoutInfo.visibleItemsInfo.getOrNull(draggedItem.intValue - listState.firstVisibleItemIndex)
                    ?: return@derivedStateOf null
            return@derivedStateOf Pair(
                item.index,
                position.floatValue - item.offset - item.size / 2f
            )
        }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier
            .then(modifier)
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        Log.d(TAG, "onDragStart: $offset")
                        listState.layoutInfo.visibleItemsInfo
                            .firstOrNull {
                                offset.y.toInt() in it.offset..(it.offset + it.size)
                            }
                            ?.also {
                                position.floatValue = it.offset + (it.size / 2f)
                            }
                    },
                    onDrag = { pic, offset ->
                        Log.d(TAG, "onDrag: $offset")
                        pic.consume()
                        position.floatValue += offset.y
                    },
                    onDragEnd = {
                        Log.d(TAG, "onDragEnd")
                        val event = ListContract.Event.UpdateItemsIndexes
                        onEventSent(event)
                        draggedItem.intValue = -1
                        position.floatValue = 0f
                    }
                )
            }
    ) {
        itemsIndexed(items) { index, task ->
            val offset by remember {
                derivedStateOf { indexWithOffset?.takeIf { it.first == index }?.second }
            }
            TaskItem(
                modifier = Modifier
                    .zIndex(offset?.let { 1f } ?: 0f)
                    .graphicsLayer {
                        translationY = offset ?: 0f
                    },
                task = task,
                onDeleteClicked = { taskToDelete ->
                    val event = ListContract.Event.RemoveTask(taskToDelete)
                    onEventSent(event)
                },
                onItemClicked = {
                    val navigation = ListContract.Effect.Navigation.ToDetails(it.id.toString())
                    onNavigationRequested(navigation)
                }
            )
            if (index != items.size - 1) {
                HorizontalDivider()
            }
        }
        item { HorizontalDivider() }
        item {
            TaskFiller(
                modifier = Modifier,
                addTaskAction = { name ->
                    val event = ListContract.Event.AddTask(name)
                    onEventSent(event)
                }
            )
        }
    }
}

@Composable
fun TaskFiller(modifier: Modifier = Modifier, addTaskAction: (name: String) -> Unit) = Row {
    val taskName = remember { mutableStateOf("") }
    TaskTextField(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        value = taskName.value,
        onValueChange = { taskName.value = it },
        keyboardActions = KeyboardActions(onDone = { addTaskAction(taskName.value) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        hint = stringResource(R.string.add_task)
    )
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