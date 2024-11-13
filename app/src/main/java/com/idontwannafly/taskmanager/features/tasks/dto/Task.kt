package com.idontwannafly.taskmanager.features.tasks.dto

import com.idontwannafly.taskmanager.features.tasks.db.dto.TaskUpdateQuery
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity

data class Task(
    val id: Long? = null,
    val name: String,
    val index: Int,
    val subTasks: List<Task> = emptyList()
) {

    companion object {
        fun fromEntity(entity: TaskEntity) : Task = Task(
            id = entity.id,
            name = entity.name,
            index = entity.index
        )
    }

    fun toEntity(parentId: Long? = null) : TaskEntity = TaskEntity(
        id = id,
        name = name,
        index = index,
        parentId = parentId
    )

    fun toUpdateQuery() : TaskUpdateQuery = TaskUpdateQuery(
        id = id!!,
        name = name,
        index = index
    )

}