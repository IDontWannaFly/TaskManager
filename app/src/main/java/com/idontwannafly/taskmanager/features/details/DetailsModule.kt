package com.idontwannafly.taskmanager.features.details

import com.idontwannafly.taskmanager.app.db.AppDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailsModule = module {
    singleOf(::DetailsLocalRepository)

    single { DetailsUseCase(get(), it.get()) }
    single { get<AppDatabase>().taskDetailsDao() }
}