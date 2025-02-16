package ru.markn.content.pres.day1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import novel_game.pkgs.content.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.annotation.KoinViewModel
import ru.markn.engine.audio.AudioPlayer
import ru.markn.engine.GameEngineOps
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class Day1Processor(
    private val engineOps: GameEngineOps
) : ViewModel() {
    private val _state = MutableStateFlow(Day1UIState())
    val state = _state.asStateFlow()

    private var jobStage: Job? = null
    private var audioPlayer: AudioPlayer? = null

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch {
            audioPlayer?.release()
        }
    }

    fun startScene() {
        engineOps.startScene()
        _state.value.stage.start()
    }

    fun onClickNextStage() {
        if (_state.value.isEndStage) {
            _state.value = _state.value.copy(isEndStage = false)
            val nextStage = Day1Stages.entries.find { it.ordinal == _state.value.stage.ordinal + 1 } ?: Day1Stages.END
            nextStage.start()
        } else {
            onEndingPhrase()
        }
    }

    fun onEndingPhrase() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isEndStage = true)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun Day1Stages.start() {
        jobStage?.cancel()
        jobStage = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (this@start) {
                    Day1Stages.START -> {
                        audioPlayer = AudioPlayer(Res.readBytes("files/day1_scene1.wav"))
                        audioPlayer?.play(true)
                        delay(2.seconds)
                        Day1Stages.OCTOBER.start()
                    }

                    Day1Stages.OCTOBER -> {
                        _state.value = _state.value.copy(stage = this@start)
                    }

                    Day1Stages.THIS_CITY_AFRAID_OF_ME -> {
                        _state.value = _state.value.copy(stage = this@start)
                    }

                    Day1Stages.BELLOW_ME_THIS_CITY -> {
                        _state.value = _state.value.copy(stage = this@start)
                    }

                    Day1Stages.WHOLE_WORLD -> {
                        _state.value = _state.value.copy(stage = this@start)
                    }

                    Day1Stages.END -> {
                        _state.value = _state.value.copy(stage = this@start)
                        delay(2.seconds)
                        engineOps.finishScene()
                    }
                }
            }
        }
    }
}