package com.idontwannafly.taskmanager.features.tasks.dto

import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity

data class Task(
    private val id: Long? = null,
    val name: String
) {

    companion object {
        fun fromEntity(entity: TaskEntity) : Task = Task(
            entity.id,
            entity.name
        )
    }

    fun toEntity() : TaskEntity = TaskEntity(
        name
    ).also { it.id = id }

}