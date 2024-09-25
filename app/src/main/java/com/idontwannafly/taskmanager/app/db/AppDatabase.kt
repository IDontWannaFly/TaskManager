package com.idontwannafly.taskmanager.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idontwannafly.taskmanager.features.details.db.TaskDetailsDao
import com.idontwannafly.taskmanager.features.details.db.entities.TaskDetailsEntity
import com.idontwannafly.taskmanager.features.tasks.db.TasksDao
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        TaskDetailsEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

    abstract fun taskDetailsDao(): TaskDetailsDao

}