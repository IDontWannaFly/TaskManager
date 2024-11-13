package com.idontwannafly.taskmanager.features.tasks.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.idontwannafly.taskmanager.features.tasks.db.dto.TaskUpdateQuery
import com.idontwannafly.taskmanager.features.tasks.db.entities.TaskEntity
import com.idontwannafly.taskmanager.features.tasks.dto.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("select * from taskentity where parentId is :parentId order by `index`")
    fun getTasksFlow(parentId: Long? = null) : Flow<List<TaskEntity>>

    @Query("select * from taskentity where id = :taskId limit 1")
    suspend fun getTask(taskId: Long) : TaskEntity?

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update(
        entity = TaskEntity::class
    )
    suspend fun updateTask(query: TaskUpdateQuery)

    @Update(
        entity = TaskEntity::class
    )
    suspend fun updateTasks(queries: List<TaskUpdateQuery>)

}