package com.idontwannafly.taskmanager.features.tasks.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("select * from taskentity order by id")
    fun getTasksFlow() : Flow<List<TaskEntity>>

    @Query("select * from taskentity")
    suspend fun getTasks() : List<TaskEntity>

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update
    suspend fun updateTask(entity: TaskEntity)

    @Update
    suspend fun updateTasks(entities: List<TaskEntity>)

}