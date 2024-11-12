package com.idontwannafly.taskmanager.ui.screens.list

import com.idontwannafly.taskmanager.ui.base.ViewEffect
import com.idontwannafly.taskmanager.ui.base.ViewEvent
import com.idontwannafly.taskmanager.ui.base.ViewState
import com.idontwannafly.taskmanager.features.tasks.dto.Task

class ListContract {

    sealed class Event : ViewEvent {
        data class RemoveTask(val task: Task) : Event()
        data class AddTask(val name: String) : Event()
        data class MoveItems(val fromIdx: Int, val toIdx: Int) : Event()
        data object UpdateItemsIndexes : Event()
    }

    data class State(
        val tasks: List<Task>
    ) : ViewState

    sealed class Effect : ViewEffect {
        sealed class Navigation : Effect() {
            class ToDetails(val taskId: String) : Navigation()
        }
    }

}