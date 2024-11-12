package com.idontwannafly.taskmanager.app.extensions.eventprocessor

import java.time.Clock

private const val DEBOUNCE_TIME_MILLIS = 1000L

class DebounceEventProcessor : EventProcessor {

    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTime: Long = 0L

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTime >= DEBOUNCE_TIME_MILLIS) {
            event.invoke()
        }
        lastEventTime = now
    }
}