package com.idontwannafly.taskmanager.ui.screens.list.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onItemClicked: (Task) -> Unit = {},
    onDeleteClicked: (Task) -> Unit = {},
    onExpanded: (isExpanded: Boolean) -> Unit
) = Row(
    modifier = Modifier
        .then(modifier)
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .clickable { onItemClicked(task) },
    verticalAlignment = Alignment.CenterVertically,
) {
    val expanded by remember(task.subTasks.size) { mutableStateOf(task.subTasks.isNotEmpty()) }
    val expandedIcon by remember {
        derivedStateOf {
            if (expanded) Icons.Default.KeyboardArrowDown
            else Icons.AutoMirrored.Filled.KeyboardArrowRight
        }
    }
    Icon(
        imageVector = expandedIcon,
        contentDescription = null,
        modifier = Modifier
            .clickable { onExpanded(!expanded) }
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

@Preview
@Composable
fun TaskItemPreview() = Surface(
) {
    TaskItem(task = Task(name = "Simple name", index = 0)) {}
}