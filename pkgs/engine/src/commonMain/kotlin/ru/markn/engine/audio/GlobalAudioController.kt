package ru.markn.engine.audio

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GlobalAudioController {
    private val _isAudioEnabledFlow = MutableStateFlow(true)
    val isAudioEnabledFlow: StateFlow<Boolean>
        get() = _isAudioEnabledFlow.asStateFlow()

    fun setAudioEnabled(isEnabled: Boolean) {
        _isAudioEnabledFlow.value = isEnabled
    }
}