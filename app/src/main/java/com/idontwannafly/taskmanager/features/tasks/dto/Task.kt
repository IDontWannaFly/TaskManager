package com.idontwannafly.taskmanager.features.tasks.dto

import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity

data class Task(
    val name: String
) {

    fun toEntity() : TaskEntity = TaskEntity(
        name
    )

}