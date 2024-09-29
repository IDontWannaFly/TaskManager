package com.idontwannafly.taskmanager.features.details.dto

import com.idontwannafly.taskmanager.features.details.db.entities.TaskDetailsEntity
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity
import com.idontwannafly.taskmanager.features.tasks.dto.Task

data class TaskDetails(
    private val taskId: Long,
    val name: String,
    val description: String
) {

    companion object {
        fun fromEntity(entity: TaskDetailsEntity) = TaskDetails(
            taskId = entity.taskId,
            name = entity.name,
            description = entity.description
        )

        fun empty() = TaskDetails(-1, "", "")

        fun mock() = TaskDetails(-1, "Some task name","Some mock description")

        fun fromTaskEntity(task: TaskEntity) = TaskDetails(
            taskId = task.id ?: -1L,
            name = task.name,
            description = ""
        )
    }

    fun toEntity() = TaskDetailsEntity(
        taskId = taskId,
        description = description,
        name = name
    )

}