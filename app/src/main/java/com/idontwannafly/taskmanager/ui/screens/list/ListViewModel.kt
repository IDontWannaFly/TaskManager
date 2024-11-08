package com.idontwannafly.taskmanager.ui.screens.list

import android.util.Log
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
) : BaseViewModel<ListContract.State, ListContract.Event, ListContract.Effect>() {

    private val tasks = mutableListOf<Task>()

    private val _tasksFlow = MutableStateFlow<List<Task>>(listOf())
    val tasksFlow = _tasksFlow.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        useCase.getTasksFlow().collect(viewModelScope) { items ->
            updateTaskIndexes(items)
            val sortedList = items.sortedBy { it.index }
            tasks.clear()
            tasks.addAll(sortedList)
            _tasksFlow.emit(sortedList)
        }
    }

    private suspend fun updateTaskIndexes(items: List<Task>) {
        val itemsToUpdate = mutableListOf<Task>()
        items.forEachIndexed { index, task ->
            if (index == task.index) return@forEachIndexed
            Log.d(TAG, "Index: $index; Task: $task")
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

    fun updateItemsIndexes() {
        viewModelScope.launch {
            updateTaskIndexes(tasks)
        }
    }

    fun moveItem(fromIdx: Int, toIdx: Int) {
        viewModelScope.launch {
            val itemFrom = tasks[fromIdx]
            val itemTo = tasks[toIdx]
            Log.d(TAG, "Move items: \n" +
                    "\tFrom: Index - $fromIdx; Item: $itemFrom;\n" +
                    "\tTo: Index - $toIdx; Item: $itemTo")
            tasks[fromIdx] = itemTo
            tasks[toIdx] = itemFrom
            _tasksFlow.emit(tasks)
        }
    }

    override fun getInitialState(): ListContract.State = ListContract.State(emptyList())

    override suspend fun handleEvent(event: ListContract.Event) {
        when (event) {
            else -> Unit
        }
    }

    companion object {
        private const val TAG = "ListViewModel"
    }

}