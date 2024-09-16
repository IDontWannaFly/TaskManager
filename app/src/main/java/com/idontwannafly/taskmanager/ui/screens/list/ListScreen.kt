package com.idontwannafly.taskmanager.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.app.modules
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import com.idontwannafly.taskmanager.ui.screens.list.item.TaskItem
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication

@Composable
fun ListScreen(viewModel: ListViewModel = koinViewModel()) = Surface(Modifier.fillMaxSize()) {
    val items = viewModel.tasksFlow.collectAsState()
    ListScreenContent(items.value, viewModel::addTask, viewModel::removeTask)
}

@Composable
fun ListScreenContent(items: List<Task>, addTaskAction: (name: String) -> Unit, removeTaskAction: (Task) -> Unit) = Column {
    Header()
    TaskList(items, removeTaskAction)
    TaskFiller(addTaskAction)
}

@Composable
fun Header() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        text = stringResource(R.string.tasks_list).uppercase(),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun TaskList(items: List<Task>, removeTaskAction: (Task) -> Unit) = LazyColumn {
    itemsIndexed(items) { index, task ->
        TaskItem(task, onDeleteClicked = removeTaskAction)
        if (index != items.size - 1) {
            HorizontalDivider()
        }
    }
}

@Composable
fun TaskFiller(addTaskAction: (name: String) -> Unit) = Row {
    val taskName = remember { mutableStateOf("") }
    TextField(
        value = taskName.value,
        placeholder = { Text("Add new task") },
        onValueChange = { name -> taskName.value = name },
        keyboardActions = KeyboardActions(onDone = { addTaskAction(taskName.value) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        textStyle = LocalTextStyle.current,
        colors = TextFieldDefaults.colors()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListScreenPreview() = Surface(Modifier.fillMaxSize()) {
    val items = remember { mutableStateOf(listOf(Task(name = "Simple task"))) }
    ListScreenContent(items.value, {}, {})
}