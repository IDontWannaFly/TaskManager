package com.idontwannafly.taskmanager

import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected val TAG = javaClass.simpleName

    init {
        Log.d(TAG, "Init")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
}