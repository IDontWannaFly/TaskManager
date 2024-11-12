package com.idontwannafly.taskmanager.ui.screens.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idontwannafly.taskmanager.ui.base.BaseViewModel
import com.idontwannafly.taskmanager.app.extensions.collect
import com.idontwannafly.taskmanager.features.tasks.TasksUseCase
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import kotlinx.coroutines.launch

class ListViewModel(
    private val useCase: TasksUseCase
) : BaseViewModel<ListContract.State, ListContract.Event, ListContract.Effect>() {

    private val tasks = mutableListOf<Task>()

    private val tasksList: List<Task>
        get() = viewState.value.tasks

    init {
        observeTasks()
    }

    private fun observeTasks() {
        useCase.getTasksFlow().collect(viewModelScope) { items ->
            updateTaskIndexes(items)
            val sortedList = items.sortedBy { it.index }
            tasks.clear()
            tasks.addAll(sortedList)
            updateTasksList(sortedList)
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

    private fun addTask(name: String) {
        viewModelScope.launch {
            val index = tasksList.size
            useCase.addTask(name, index)
        }
    }

    private fun removeTask(task: Task) {
        viewModelScope.launch {
            useCase.deleteTask(task)
        }
    }

    private fun updateItemsIndexes() {
        viewModelScope.launch {
            updateTaskIndexes(tasks)
        }
    }

    private fun moveItem(fromIdx: Int, toIdx: Int) {
        viewModelScope.launch {
            val itemFrom = tasks[fromIdx]
            val itemTo = tasks[toIdx]
            Log.d(TAG, "Move items: \n" +
                    "\tFrom: Index - $fromIdx; Item: $itemFrom;\n" +
                    "\tTo: Index - $toIdx; Item: $itemTo")
            tasks[fromIdx] = itemTo
            tasks[toIdx] = itemFrom
            updateTasksList(tasks)
        }
    }

    private fun updateTasksList(list: List<Task>) {
        setState { copy(tasks = list) }
    }

    override fun provideInitialState(): ListContract.State = ListContract.State(emptyList())

    override suspend fun handleEvent(event: ListContract.Event) {
        when (event) {
            is ListContract.Event.AddTask -> addTask(event.name)
            is ListContract.Event.MoveItems -> moveItem(event.fromIdx, event.toIdx)
            is ListContract.Event.RemoveTask -> removeTask(event.task)
            is ListContract.Event.UpdateItemsIndexes -> updateItemsIndexes()
            is ListContract.Event.SelectTask -> setEffect { ListContract.Effect.Navigation.ToDetails(event.task.id.toString()) }
        }
    }

    companion object {
        private const val TAG = "ListViewModel"
    }

}