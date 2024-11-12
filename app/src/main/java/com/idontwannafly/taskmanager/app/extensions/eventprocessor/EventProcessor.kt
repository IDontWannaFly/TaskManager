package com.idontwannafly.taskmanager.app.extensions.eventprocessor

interface EventProcessor {

    fun processEvent(event: () -> Unit)

    companion object {
        private val processorsMap = mutableMapOf<String, EventProcessor>()

        fun get(id: String, builder: () -> EventProcessor) : EventProcessor {
            return processorsMap.getOrPut(id) { builder() }
        }
    }

}