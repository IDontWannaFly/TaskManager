package com.idontwannafly.taskmanager.features.tasks

import com.idontwannafly.taskmanager.features.tasks.db.TasksDao
import com.idontwannafly.taskmanager.features.tasks.dto.Task

class TasksLocalRepository(
    private val dao: TasksDao
) {

    suspend fun addTask(task: Task) {
        val entity = task.toEntity()
        dao.insertTask(entity)
    }

}