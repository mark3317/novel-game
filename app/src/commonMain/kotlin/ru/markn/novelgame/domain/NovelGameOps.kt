package ru.markn.novelgame.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import ru.markn.engine.GameEngineOps

@Single
class NovelGameOps(
    private val engineOps: GameEngineOps
) {
    private val _currentSceneFlow = MutableStateFlow(Scenes.DAY1)
    val currentSceneFlow = _currentSceneFlow.asStateFlow()

    private val _isFinishedGameFlow = MutableStateFlow(false)
    val isFinishedGameFlow = _isFinishedGameFlow.asStateFlow()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            engineOps.isFinishedSceneFlow.collect { isFinishedScene ->
                if (isFinishedScene) {
                    Scenes.entries.find { it.ordinal == _currentSceneFlow.value.ordinal + 1 }?.let {
                        _currentSceneFlow.value = it
                    } ?: run {
                        _isFinishedGameFlow.value = true
                    }
                }
            }
        }
    }

    fun restartGame() {
        _currentSceneFlow.value = Scenes.DAY1
        _isFinishedGameFlow.value = false
    }
}