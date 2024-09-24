package com.idontwannafly.taskmanager.ui.screens.list

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
        useCase.getTasksFlow().collect(viewModelScope) { items ->
            updateTaskIndexes(items)
            val sortedList = items.sortedBy { it.index }
            _tasksFlow.emit(sortedList)
        }
    }

    private suspend fun updateTaskIndexes(items: List<Task>) {
        val itemsToUpdate = mutableListOf<Task>()
        items.forEachIndexed { index, task ->
            if (index != -1) return@forEachIndexed
            val indexedTask = task.copy(index = index)
            itemsToUpdate.add(indexedTask)
        }
        useCase.updateTasks(itemsToUpdate)
    }

    fun addTask(name: String) {
        viewModelScope.launch {
            val index = _tasksFlow.value.size
            useCase.addTask(name, index)
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch {
            useCase.deleteTask(task)
        }
    }

    fun rearrangeItems(items: List<Task>) {

    }

}