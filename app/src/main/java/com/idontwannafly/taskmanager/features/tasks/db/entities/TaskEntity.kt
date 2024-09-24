package com.idontwannafly.taskmanager.features.tasks.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    val name: String,
    val index: Int = -1
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}