package com.idontwannafly.taskmanager.app

import com.idontwannafly.taskmanager.features.tasks.tasksModule
import com.idontwannafly.taskmanager.ui.viewModelModule

val modules = listOf(
    appModule,
    tasksModule,
    viewModelModule
)