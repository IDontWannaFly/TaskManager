package com.idontwannafly.taskmanager.features.tasks

import com.idontwannafly.taskmanager.features.tasks.dto.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TasksUseCase(
    private val repository: TasksLocalRepository
) {

    fun getTasksFlow(parentId: Long? = null): Flow<List<Task>> = repository.getFlow(parentId)

    suspend fun addTask(parentId: Long?, name: String, index: Int) = withContext(Dispatchers.IO) {
        val task = Task(name = name, index = index)
        repository.addTask(parentId, task)
    }

    suspend fun deleteTask(task: Task) = withContext(Dispatchers.IO) {
        repository.deleteTask(task)
    }

    suspend fun updateTask(newTask: Task) = withContext(Dispatchers.IO) {
        repository.updateTask(newTask)
    }

    suspend fun updateTasks(itemsToUpdate: List<Task>) = withContext(Dispatchers.IO) {
        repository.updateTasks(itemsToUpdate)
    }

}