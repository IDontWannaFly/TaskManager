package com.idontwannafly.taskmanager.ui.screens.details

import com.idontwannafly.taskmanager.ui.base.ViewEffect
import com.idontwannafly.taskmanager.ui.base.ViewEvent
import com.idontwannafly.taskmanager.ui.base.ViewState
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails

class DetailsContract {

    sealed class Event : ViewEvent {
        data class UpdateDetails(val details: TaskDetails) : Event()
        data object NavigateToList : Event()
    }

    data class State(
        val details: TaskDetails,
        val isLoading: Boolean
    ) : ViewState

    sealed class Effect : ViewEffect {
        sealed class Navigation : Effect() {
            data object ToList : Navigation()
        }
    }

}