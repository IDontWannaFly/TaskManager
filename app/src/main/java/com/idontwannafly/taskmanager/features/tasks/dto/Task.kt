package com.idontwannafly.taskmanager.features.tasks.dto

import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity

data class Task(
    private val id: Long? = null,
    val name: String,
    val index: Int
) {

    companion object {
        fun fromEntity(entity: TaskEntity) : Task = Task(
            id = entity.id,
            name = entity.name,
            index = entity.index
        )
    }

    fun toEntity() : TaskEntity = TaskEntity(
        name,
        index
    ).also { it.id = id }

}