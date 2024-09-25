package com.idontwannafly.taskmanager.ui.screens.list

import android.annotation.SuppressLint
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.app.extensions.collect
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import com.idontwannafly.taskmanager.ui.screens.list.item.TaskItem
import com.idontwannafly.taskmanager.ui.views.TaskTextField
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

private const val TAG = "ListScreen"

@Composable
fun ListScreen(viewModel: ListViewModel = koinViewModel()) = Surface(Modifier.fillMaxSize()) {
    val items by viewModel.tasksFlow.collectAsState()
    ListScreenContent(
        items,
        viewModel::addTask,
        viewModel::removeTask,
        viewModel::moveItem,
        viewModel::updateItemsIndexes
    )
}

@Composable
fun ListScreenContent(
    items: List<Task>,
    addTaskAction: (name: String) -> Unit,
    removeTaskAction: (Task) -> Unit,
    moveItems: (fromIdx: Int, toIdx: Int) -> Unit,
    updateItemsIndexes: () -> Unit
) = Column(
    Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
) {
    Header()
    TaskList(items, removeTaskAction, addTaskAction, moveItems, updateItemsIndexes)
}

@Composable
fun Header() {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = stringResource(R.string.tasks_list).uppercase(),
        textAlign = TextAlign.Center,
    )
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun TaskList(
    items: List<Task>,
    removeTaskAction: (Task) -> Unit,
    addTaskAction: (name: String) -> Unit,
    moveItems: (fromIdx: Int, toIdx: Int) -> Unit,
    updateItemsIndexes: () -> Unit
) {
    val listState = rememberLazyListState()
    val position = remember { mutableFloatStateOf(0f) }
    val draggedItem = remember { mutableIntStateOf(-1) }
    val scope = rememberCoroutineScope()
    snapshotFlow { listState.layoutInfo }
        .combine(snapshotFlow { position.floatValue }) { state, pos ->
            Log.d(TAG, "Position: $pos")
            if (pos == 0f) return@combine -1
            val nearestItem =
                state.visibleItemsInfo.minByOrNull { (pos - (it.offset + it.size / 2f)).absoluteValue }
            return@combine nearestItem?.index ?: -1
        }
        .distinctUntilChanged()
        .collect(scope) { near ->
            Log.d(TAG, "Dragged item: ${draggedItem.intValue}; Near: $near")
            draggedItem.intValue = when {
                near == -1 -> -1
                draggedItem.intValue == -1 -> near
                else -> near.also { moveItems(draggedItem.intValue, near) }
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
            .fillMaxSize()
            .padding(vertical = 10.dp)
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
                        updateItemsIndexes()
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
                onDeleteClicked = removeTaskAction
            )
            if (index != items.size - 1) {
                HorizontalDivider()
            }
        }
        item { HorizontalDivider() }
        item {
            TaskFiller(
                modifier = Modifier,
                addTaskAction = addTaskAction
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
    val items = remember {
        mutableStateOf(
            listOf(
                Task(name = "Simple task", index = 0),
                Task(name = "Second task", index = 1)
            )
        )
    }
    ListScreenContent(items.value.toMutableList(), {}, {}, { _, _ -> }, {})
}