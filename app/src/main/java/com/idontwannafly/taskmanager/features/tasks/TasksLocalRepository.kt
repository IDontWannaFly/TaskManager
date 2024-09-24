package com.idontwannafly.taskmanager.features.tasks

import com.idontwannafly.taskmanager.features.tasks.db.TasksDao
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class TasksLocalRepository(
    private val dao: TasksDao
) {

    fun getFlow(): Flow<List<Task>> {
        return dao.getTasksFlow().map { it.map { Task.fromEntity(it) } }
    }

    suspend fun addTask(task: Task) {
        val entity = task.toEntity()
        dao.insertTask(entity)
    }

    suspend fun deleteTask(task: Task) {
        val entity = task.toEntity()
        dao.deleteTask(entity)
    }

    suspend fun updateTask(newTask: Task) {
        val entity = newTask.toEntity()
        dao.updateTask(entity)
    }

    suspend fun updateTasks(itemsToUpdate: List<Task>) {
        val entities = itemsToUpdate.map { it.toEntity() }
        dao.updateTasks(entities)
    }

}