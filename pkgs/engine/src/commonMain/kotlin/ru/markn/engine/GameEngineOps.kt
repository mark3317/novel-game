package ru.markn.engine

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single

@Single
class GameEngineOps {
    private val _isFinishedSceneFlow = MutableStateFlow(false)
    val isFinishedSceneFlow = _isFinishedSceneFlow.asStateFlow()

    fun startScene() {
        _isFinishedSceneFlow.value = false
    }

    fun finishScene() {
        _isFinishedSceneFlow.value = true
    }
}