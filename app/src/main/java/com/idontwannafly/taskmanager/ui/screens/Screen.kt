package com.idontwannafly.taskmanager.ui.screens

sealed class Screen(val route: String) {

    data object List : Screen("list")
    data object Details : Screen("details")

}