package com.idontwannafly.taskmanager.ui.screens.list.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.features.tasks.dto.Task

fun LazyListScope.TaskItem(
    modifier: Modifier = Modifier,
    isOdd: Boolean,
    task: Task,
    onItemClicked: (Task) -> Unit = {},
    onDeleteClicked: (Task) -> Unit = {},
    addTaskAction: (name: String, parentId: Long?) -> Unit = { _, _ -> },
    onExpanded: (isExpanded: Boolean) -> Unit
) {
    item {
        Row(
            modifier = Modifier
                .background(
                    if (isOdd) MaterialTheme.colorScheme.background
                    else Color.LightGray.copy(alpha = 0.5f)
                )
                .then(modifier)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clickable { onItemClicked(task) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (task.isExpanded) Icons.Default.KeyboardArrowDown
                else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .clickable { onExpanded(!task.isExpanded) }
                    .padding(5.dp)
                    .width(24.dp)
                    .height(24.dp)
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 5.dp)
                    .padding(end = 5.dp),
                text = task.name,
                maxLines = 1,
            )
            Text(
                modifier = Modifier
                    .clickable { onDeleteClicked(task) }
                    .padding(vertical = 5.dp, horizontal = 5.dp),
                text = stringResource(R.string.remove),
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    }
    if (!task.isExpanded) return
    task.subTasks.forEachIndexed { index, subTask ->
        TaskItem(
            modifier = modifier
                .padding(start = 34.dp),
            isOdd = index % 2 == 0,
            task = subTask,
            onItemClicked = onItemClicked,
            onDeleteClicked = onDeleteClicked,
            addTaskAction = addTaskAction,
            onExpanded = onExpanded
        )
    }
    item {
        TaskFiller(
            modifier = Modifier
                .padding(start = 34.dp),
            addTaskAction = { name ->
                addTaskAction(name, task.id)
            }
        )
    }
}

@Preview
@Composable
fun TaskItemPreview() = LazyColumn(
) {
    TaskItem(
        modifier = Modifier,
        isOdd = false,
        task = Task(name = "Simple name", index = 0),
        onItemClicked = {},
        onDeleteClicked = {},
        onExpanded = {}
    )
}