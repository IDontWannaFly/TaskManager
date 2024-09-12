package com.idontwannafly.taskmanager.app

import android.content.Context
import androidx.room.Room
import com.idontwannafly.taskmanager.app.db.AppDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::provideAppDatabase)

    single { get<AppDatabase>().tasksDao() }
}

private fun provideAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "main_database")
        .fallbackToDestructiveMigration()
        .build()
}