package com.idontwannafly.taskmanager.ui.screens.list.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.features.tasks.dto.Task

@Composable
fun TaskItem(
    task: Task,
    onItemClicked: (Task) -> Unit = {},
    onDeleteClicked: (Task) -> Unit = {}) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .clickable { onItemClicked(task) },
    verticalAlignment = Alignment.CenterVertically,
) {
    Text(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        text = task.name
    )
    Text(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .wrapContentHeight()
            .clickable { onDeleteClicked(task) },
        text = stringResource(R.string.remove),
        color = Color.Red,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(Task(name = "Simple name"))
}