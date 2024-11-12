package com.idontwannafly.taskmanager.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.idontwannafly.taskmanager.app.extensions.eventprocessor.DebounceEventProcessor
import com.idontwannafly.taskmanager.app.extensions.eventprocessor.EventProcessor
import java.util.UUID

@Composable
fun debouncedClick(
    id: String = UUID.randomUUID().toString(),
    onClick: () -> Unit
) : () -> Unit {
    val debounceEventProcessor = remember { EventProcessor.get(id) { DebounceEventProcessor() } }
    val newOnClick: () -> Unit = {
        debounceEventProcessor.processEvent { onClick() }
    }
    return newOnClick
}