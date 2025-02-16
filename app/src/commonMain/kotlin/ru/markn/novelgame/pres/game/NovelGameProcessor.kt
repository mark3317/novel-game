package ru.markn.novelgame.pres.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.markn.novelgame.domain.NovelGameOps

@KoinViewModel
class NovelGameProcessor(
    private val gameOps: NovelGameOps
) : ViewModel() {
    private val _state = MutableStateFlow(NovelGameUIState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            gameOps.currentSceneFlow.collect { scene ->
                _state.value = _state.value.copy(scene = scene)
            }
        }
        viewModelScope.launch {
            gameOps.isFinishedGameFlow.collect { isFinished ->
                if (isFinished) {
                    _state.value = _state.value.copy(isFinishedGame = true)
                }
            }
        }
    }
}