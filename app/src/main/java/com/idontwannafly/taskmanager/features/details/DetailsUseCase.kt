package com.idontwannafly.taskmanager.features.details

import com.idontwannafly.taskmanager.features.details.dto.TaskDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailsUseCase(
    private val repository: DetailsLocalRepository,
    private val taskId: Long
) {

    suspend fun getDetails(): TaskDetails = withContext(Dispatchers.IO) {
        return@withContext repository.getDetails(taskId)
    }

    suspend fun updateDetails(details: TaskDetails) = withContext(Dispatchers.IO) {
        repository.updateDetails(details)
    }

}