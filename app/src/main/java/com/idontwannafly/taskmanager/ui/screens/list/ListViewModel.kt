package com.idontwannafly.taskmanager.ui.screens.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.idontwannafly.taskmanager.BaseViewModel
import com.idontwannafly.taskmanager.app.extensions.collect
import com.idontwannafly.taskmanager.features.tasks.TasksUseCase
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val useCase: TasksUseCase
) : BaseViewModel() {

    private val _tasksFlow = MutableStateFlow<List<Task>>(listOf())
    val tasksFlow = _tasksFlow.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        useCase.getTasksFlow().collect(viewModelScope) { _tasksFlow.emit(it) }
    }

    fun addTask(name: String) {
        viewModelScope.launch {
            useCase.addTask(name)
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch {
            useCase.deleteTask(task)
        }
    }

}