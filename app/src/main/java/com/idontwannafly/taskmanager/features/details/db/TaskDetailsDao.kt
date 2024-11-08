package com.idontwannafly.taskmanager.features.details.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.idontwannafly.taskmanager.features.details.db.entities.TaskDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDetailsDao {

    @Query("select * from taskdetailsentity where taskId = :taskId")
    suspend fun getDetails(taskId: Long) : TaskDetailsEntity?

    @Query("select * from taskdetailsentity where taskId = :taskId")
    fun getDetailsFlow(taskId: Long) : Flow<TaskDetailsEntity?>

    @Insert
    suspend fun addDetails(entity: TaskDetailsEntity)

    @Update
    suspend fun updateDetails(entity: TaskDetailsEntity)

}