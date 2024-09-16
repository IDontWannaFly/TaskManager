package com.idontwannafly.taskmanager.ui

import com.idontwannafly.taskmanager.ui.screens.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ListViewModel)
}