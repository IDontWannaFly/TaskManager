package com.idontwannafly.taskmanager.features.tasks.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("select * from taskentity")
    fun getTasksFlow() : Flow<List<TaskEntity>>

    @Query("select * from taskentity")
    suspend fun getTasks() : List<TaskEntity>

    @Insert
    suspend fun insertTask(task: TaskEntity)

}