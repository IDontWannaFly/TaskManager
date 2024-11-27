package com.idontwannafly.taskmanager.ui.screens.list.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import com.idontwannafly.taskmanager.ui.screens.list.ListContract
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.absoluteValue

@Composable
fun TasksList(
    modifier: Modifier,
    items: List<Task>,
    onEventSent: (event: ListContract.Event) -> Unit
) {
    val listState = rememberLazyListState()
    val position = remember { mutableFloatStateOf(0f) }
    val draggedItem = remember { mutableIntStateOf(-1) }
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { listState.layoutInfo }
            .combine(snapshotFlow { position.floatValue }) { state, pos ->
                if (pos == 0f) return@combine -1
                val nearestItem =
                    state.visibleItemsInfo.minByOrNull { (pos - (it.offset + it.size / 2f)).absoluteValue }
                return@combine nearestItem?.index ?: -1
            }
            .distinctUntilChanged()
            .collect { near ->
                draggedItem.intValue = when {
                    near == -1 -> -1
                    draggedItem.intValue == -1 -> near
                    near >= items.size -> draggedItem.intValue
                    else -> near.also {
                        val event = ListContract.Event.MoveItems(draggedItem.intValue, near)
                        onEventSent(event)
                    }
                }
            }
    }
    val indexWithOffset by remember {
        derivedStateOf {
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
                        listState.layoutInfo.visibleItemsInfo
                            .firstOrNull {
                                offset.y.toInt() in it.offset..(it.offset + it.size)
                            }
                            ?.also {
                                position.floatValue = it.offset + (it.size / 2f)
                            }
                    },
                    onDrag = { pic, offset ->
                        pic.consume()
                        if (position.floatValue > (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.offset ?: 0)) {
                            position.floatValue = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.offset?.toFloat() ?: position.floatValue
                            return@detectDragGesturesAfterLongPress
                        }
                        position.floatValue += offset.y
                    },
                    onDragEnd = {
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
            val isOdd by remember {
                mutableStateOf(index % 2 == 0)
            }
            TaskItem(
                modifier = Modifier
                    .background(
                        if (isOdd) MaterialTheme.colorScheme.background
                        else Color.LightGray.copy(alpha = 0.5f)
                    )
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
                    val event = ListContract.Event.SelectTask(it)
                    onEventSent(event)
                },
                onExpanded = { isExpanded ->
                    val event = if (isExpanded) ListContract.Event.GetSubtasks(task)
                    else ListContract.Event.ClearSubtasks(task)
                    onEventSent(event)
                }
            )
        }
        item {
            TaskFiller(
                modifier = Modifier,
                addTaskAction = { name ->
                    val event = ListContract.Event.AddTask(parentId = null, name = name)
                    onEventSent(event)
                }
            )
        }
    }
}