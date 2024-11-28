package com.idontwannafly.taskmanager.ui.screens.list.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.idontwannafly.taskmanager.R
import com.idontwannafly.taskmanager.ui.views.TaskTextField

@Composable
fun TaskFiller(modifier: Modifier = Modifier, addTaskAction: (name: String) -> Unit) = Row {
    val taskName = remember { mutableStateOf("") }
    TaskTextField(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(5.dp),
        value = taskName.value,
        onValueChange = { taskName.value = it },
        keyboardActions = KeyboardActions(onDone = { addTaskAction(taskName.value) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        hint = stringResource(R.string.add_task)
    )
}