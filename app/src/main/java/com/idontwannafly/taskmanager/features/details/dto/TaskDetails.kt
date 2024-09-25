package com.idontwannafly.taskmanager.features.details.dto

import com.idontwannafly.taskmanager.features.details.db.entities.TaskDetailsEntity
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity

data class TaskDetails(
    private val taskId: Long,
    val description: String
) {

    companion object {
        fun fromEntity(entity: TaskDetailsEntity) = TaskDetails(
            taskId = entity.taskId,
            description = entity.description
        )

        fun empty() = TaskDetails(-1, "")
    }

    fun toEntity() = TaskDetailsEntity(
        taskId = taskId,
        description = description
    )

}