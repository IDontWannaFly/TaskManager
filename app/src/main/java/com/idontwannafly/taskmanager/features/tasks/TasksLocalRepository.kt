package com.idontwannafly.taskmanager.features.tasks

import android.util.Log
import com.idontwannafly.taskmanager.features.tasks.db.TasksDao
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksLocalRepository(
    private val dao: TasksDao
) {

    fun getFlow(parentId: Long? = null): Flow<List<Task>> {
        return dao.getTasksFlow(parentId).map { it.map { entity -> Task.fromEntity(entity) } }
    }

    suspend fun addTask(parentId: Long?, task: Task) {
        val entity = task.toEntity(parentId)
        try {
            dao.insertTask(entity)
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.message, e)
        }
    }

    suspend fun deleteTask(task: Task) {
        val entity = task.toEntity()
        dao.deleteTask(entity)
    }

    suspend fun updateTask(newTask: Task) {
        val query = newTask.toUpdateQuery()
        dao.updateTask(query)
    }

    suspend fun updateTasks(itemsToUpdate: List<Task>) {
        val queries = itemsToUpdate.map { it.toUpdateQuery() }
        dao.updateTasks(queries)
    }

}