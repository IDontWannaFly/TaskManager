package com.idontwannafly.taskmanager.ui.screens.list

import com.idontwannafly.taskmanager.ViewEffect
import com.idontwannafly.taskmanager.ViewEvent
import com.idontwannafly.taskmanager.ViewState
import com.idontwannafly.taskmanager.features.tasks.dto.Task

class ListContract {

    sealed class Event : ViewEvent

    data class State(
        val tasks: List<Task>
    ) : ViewState

    sealed class Effect : ViewEffect

}