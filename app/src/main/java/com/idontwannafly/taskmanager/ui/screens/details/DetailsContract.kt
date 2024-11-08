package com.idontwannafly.taskmanager.ui.screens.details

import com.idontwannafly.taskmanager.ViewEffect
import com.idontwannafly.taskmanager.ViewEvent
import com.idontwannafly.taskmanager.ViewState
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails

class DetailsContract {

    sealed class Event : ViewEvent

    data class State(
        val details: TaskDetails,
        val isLoading: Boolean
    ) : ViewState

    sealed class Effect : ViewEffect

}