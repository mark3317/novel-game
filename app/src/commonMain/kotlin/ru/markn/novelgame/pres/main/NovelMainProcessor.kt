package ru.markn.novelgame.pres.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel
import novel_game.app.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ru.markn.engine.audio.AudioPlayer
import ru.markn.novelgame.domain.NovelGameOps
import ru.markn.engine.utils.exitProgram
import ru.markn.engine.utils.getPlatform

@OptIn(ExperimentalResourceApi::class)
@KoinViewModel
class NovelMainProcessor(
    private val ops: NovelGameOps
) : ViewModel() {
    private val _state = MutableStateFlow(NovelMainUIState())
    val state = _state.asStateFlow()

    private var audioPlayer: AudioPlayer? = null

    init {
        _state.value = _state.value.copy(text = "Hello Compose: ${getPlatform()}")
    }

    fun onClickShowButton() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isShowedText = !state.value.isShowedText)
        }
    }

    fun playMusic() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                audioPlayer = AudioPlayer(Res.readBytes("files/music1.wav"))
                audioPlayer?.play(true)
            }
        }
    }

    fun startGame() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ops.restartGame()
                audioPlayer?.release()
            }
        }
    }

    fun onClickExitButton() {
        exitProgram()
    }
}