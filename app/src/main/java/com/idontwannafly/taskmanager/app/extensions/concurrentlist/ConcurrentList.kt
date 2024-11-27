package com.idontwannafly.taskmanager.app.extensions.concurrentlist

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ConcurrentList<T>(
    items: List<T>
) {

    private val list: MutableList<T> = items.toMutableList()
    private val _mutex = Mutex()

    suspend fun get(): List<T> = _mutex.withLock {
        return@withLock list.toList()
    }

    suspend fun get(index: Int): T = _mutex.withLock {
        return@withLock list[index]
    }

    suspend fun add(element: T) = _mutex.withLock {
        list.add(element)
    }

    suspend fun add(index: Int, element: T) = _mutex.withLock {
        list.add(index, element)
    }

    suspend fun remove(element: T) = _mutex.withLock {
        list.remove(element)
    }

    suspend fun clear() = _mutex.withLock {
        list.clear()
    }

    suspend fun removeAt(index: Int) = _mutex.withLock {
        list.removeAt(index)
    }

    suspend fun set(index: Int, element: T) = _mutex.withLock {
        list.set(index, element)
    }

    suspend fun addAll(elements: Collection<T>) = _mutex.withLock {
        list.addAll(elements)
    }

    suspend fun indexOf(element: T) : Int = _mutex.withLock {
        return@withLock list.indexOf(element)
    }

}