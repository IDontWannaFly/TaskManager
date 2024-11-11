package com.idontwannafly.taskmanager.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface ViewState

interface ViewEvent

interface ViewEffect

const val SIDE_EFFECTS_KEY = "SideEffectsKey"

abstract class BaseViewModel<State : ViewState, Event : ViewEvent, Effect : ViewEffect> :
    ViewModel() {

    protected abstract fun getInitialState(): State
    protected abstract suspend fun handleEvent(event: Event)

    private val initialState by lazy { getInitialState() }

    private val _viewState = MutableStateFlow(initialState)
    val viewState = _viewState.asStateFlow()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    protected val TAG = javaClass.simpleName

    init {
        Log.d(TAG, "Init")
    }

    fun postEvent(event: Event) {
        viewModelScope.launch { handleEvent(event) }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
}