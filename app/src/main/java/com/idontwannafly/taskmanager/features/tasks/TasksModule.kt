package com.idontwannafly.taskmanager.features.tasks

import com.idontwannafly.taskmanager.app.db.AppDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val tasksModule = module {
    single { get<AppDatabase>().tasksDao() }
    singleOf(::TasksLocalRepository)
}