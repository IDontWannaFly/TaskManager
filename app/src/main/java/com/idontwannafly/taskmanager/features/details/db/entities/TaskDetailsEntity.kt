package com.idontwannafly.taskmanager.features.details.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskDetailsEntity(
    @PrimaryKey(autoGenerate = false)
    val taskId: Long,
    val name: String,
    val description: String,
)