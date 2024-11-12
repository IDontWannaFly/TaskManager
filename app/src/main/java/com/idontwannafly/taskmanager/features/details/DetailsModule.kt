package com.idontwannafly.taskmanager.features.details

import com.idontwannafly.taskmanager.app.db.AppDatabase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailsModule = module {
    factoryOf(::DetailsLocalRepository)

    factory { DetailsUseCase(get(), it.get<String>().toLong()) }
    single { get<AppDatabase>().taskDetailsDao() }
}