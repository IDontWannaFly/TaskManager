package com.idontwannafly.taskmanager.features.details

import com.idontwannafly.taskmanager.features.details.dto.TaskDetails

class DetailsUseCase(
    private val repository: DetailsLocalRepository,
    private val taskId: Long
) {

    suspend fun getDetails() : TaskDetails {
        return repository.getDetails(taskId)
    }

    suspend fun updateDetails(details: TaskDetails) {
        repository.updateDetails(details)
    }

}