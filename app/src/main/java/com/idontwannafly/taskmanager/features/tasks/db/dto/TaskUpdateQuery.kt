package com.idontwannafly.taskmanager.features.tasks.db.dto

data class TaskUpdateQuery(
    val id: Long,
    val name: String,
    val index: Int
)