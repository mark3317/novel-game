package ru.markn.engine.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration

private val mutexLock = Mutex()
private val mutexDebounceMap = mutableMapOf<String, Mutex>()
private val jobDebounceMap = mutableMapOf<String, Job>()

suspend fun CoroutineScope.withDebounce(
    key: String,
    delay: Duration,
    block: suspend () -> Unit,
) {
    mutexLock.withLock {
        mutexDebounceMap[key] = (mutexDebounceMap[key] ?: Mutex()).apply {
            if (!tryLock()) {
                println("Debounced call for key: $key")
                return
            }
        }
    }
    val isReadyUnlockFlow = MutableStateFlow(false)
    launch {
        delay(delay)
        isReadyUnlockFlow.first { true }
        mutexDebounceMap[key]?.unlock()
    }
    block()
    isReadyUnlockFlow.value = true
}

suspend fun CoroutineScope.withDelayOrCancelDebounce(
    key: String,
    delay: Duration,
    block: suspend () -> Unit,
) {
    mutexLock.withLock {
        jobDebounceMap[key]?.run {
            if (isActive) {
                cancel("Debounced call for key: $key")
                println("Debounced call for key: $key")
            }
        }
    }
    jobDebounceMap[key] = launch {
        delay(delay)
        block()
    }
    jobDebounceMap[key]?.join()
}
