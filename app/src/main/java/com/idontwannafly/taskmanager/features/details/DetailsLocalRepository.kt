package com.idontwannafly.taskmanager.features.details

import com.idontwannafly.taskmanager.features.details.db.TaskDetailsDao
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails
import com.idontwannafly.taskmanager.features.tasks.db.TasksDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailsLocalRepository(
    private val tasksDao: TasksDao,
    private val detailsDao: TaskDetailsDao
) {

    fun getFlow(taskId: Long): Flow<TaskDetails> {
        return detailsDao.getDetailsFlow(taskId)
            .map { if (it != null) TaskDetails.fromEntity(it) else createTaskDetails(taskId) }
    }

    suspend fun getDetails(taskId: Long): TaskDetails {
        val entity = detailsDao.getDetails(taskId)
        val task = entity?.let { TaskDetails.fromEntity(it) } ?: createTaskDetails(taskId)
        return task
    }

    private suspend fun createTaskDetails(taskId: Long): TaskDetails {
        val taskEntity = tasksDao.getTask(taskId)
        val details = if (taskEntity == null) TaskDetails.empty().copy(taskId = taskId)
        else TaskDetails.fromTaskEntity(taskEntity)
        addDetails(details)
        return details
    }

    private suspend fun addDetails(details: TaskDetails) {
        val entity = details.toEntity()
        detailsDao.addDetails(entity)
    }

    suspend fun updateDetails(details: TaskDetails) {
        val entity = details.toEntity()
        detailsDao.updateDetails(entity)
    }

}