package com.idontwannafly.taskmanager.features.tasks.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val index: Int = -1
)