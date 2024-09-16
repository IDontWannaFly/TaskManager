package com.idontwannafly.taskmanager.features.tasks

import com.idontwannafly.taskmanager.features.tasks.dto.Task
import kotlinx.coroutines.flow.Flow

class TasksUseCase(
    private val repository: TasksLocalRepository
) {

    fun getTasksFlow() : Flow<List<Task>> = repository.getFlow()

    suspend fun addTask(name: String) {
        val task = Task(name = name)
        repository.addTask(task)
    }

    suspend fun deleteTask(task: Task) {
        repository.deleteTask(task)
    }

}