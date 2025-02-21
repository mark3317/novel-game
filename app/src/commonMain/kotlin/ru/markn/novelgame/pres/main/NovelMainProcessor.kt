package ru.markn.novelgame.pres.main

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel
import novel_game.app.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ru.markn.engine.audio.AudioPlayer
import ru.markn.engine.mvi.MviViewModel
import ru.markn.novelgame.domain.NovelGameOps
import ru.markn.engine.utils.exitProgram
import ru.markn.engine.utils.getPlatform
import ru.markn.novelgame.domain.Game

@OptIn(ExperimentalResourceApi::class)
@KoinViewModel
class NovelMainProcessor(
    private val navController: NavController,
    private val ops: NovelGameOps
) : INovelMainActions, MviViewModel<NovelMainUIState>(
    NovelMainUIState()
) {
    private var audioPlayer: AudioPlayer? = null

    init {
        updateState {
            copy(text = "Hello Compose: ${getPlatform()}")
        }
    }

    override val observableFlows: List<Flow<*>>
        get() = listOf(
            ops.isFinishedGameFlow.onEach {
                if (it) {
                    updateState {
                        copy(text = "Game over")
                    }
                }
            }
        )

    override fun onClickShowButton() {
        updateState {
            copy(isShowedText = !isShowedText)
        }
    }

    override fun playMusic() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                audioPlayer = AudioPlayer(Res.readBytes("files/music1.wav"))
                audioPlayer?.play(true)
            }
        }
    }

    override fun startGame() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ops.restartGame()
                audioPlayer?.release()
            }
            navController.navigate(Game)
        }
    }

    override fun onClickExitButton() {
        exitProgram()
    }
}