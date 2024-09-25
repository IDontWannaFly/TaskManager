package com.idontwannafly.taskmanager.features.details

import com.idontwannafly.taskmanager.features.details.db.TaskDetailsDao
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails

class DetailsLocalRepository(
    private val dao: TaskDetailsDao
) {

    suspend fun getDetails(taskId: Long) : TaskDetails {
        val entity = dao.getDetails(taskId)
        val task = entity?.let { TaskDetails.fromEntity(it) } ?: createTaskDetails(taskId)
        return task
    }

    private suspend fun createTaskDetails(taskId: Long): TaskDetails {
        return TaskDetails(
            taskId,
            ""
        ).also { addDetails(it) }
    }

    private suspend fun addDetails(details: TaskDetails) {
        val entity = details.toEntity()
        dao.addDetails(entity)
    }

    suspend fun updateDetails(details: TaskDetails) {
        val entity = details.toEntity()
        dao.updateDetails(entity)
    }

}