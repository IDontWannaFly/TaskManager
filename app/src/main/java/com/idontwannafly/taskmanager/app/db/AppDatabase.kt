package com.idontwannafly.taskmanager.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idontwannafly.taskmanager.features.tasks.db.TasksDao
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity

@Database(
    entities = [
        TaskEntity::class
    ],
    version = 0,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tasksDao() : TasksDao

}