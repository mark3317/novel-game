package ru.markn.content.pres.day2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import novel_game.pkgs.content.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.annotation.KoinViewModel
import ru.markn.engine.audio.AudioPlayer
import ru.markn.engine.GameEngineOps
import ru.markn.engine.model.ChoiceOption
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class Day2Processor(
    private val engineOps: GameEngineOps
) : ViewModel() {
    private val _state = MutableStateFlow(Day2UIState())
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

    fun onClickOptionChoice(choiceOption: ChoiceOption) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isEndStage = false,
                choice = null
            )
            when (choiceOption) {
                is Day2Choice1Option -> {
                    when (choiceOption) {
                        Day2Choice1Option.ANSWER -> Day2Stage.CHOICE_1_PHONE_ANSWER_1.start()
                        Day2Choice1Option.RESET -> Day2Stage.CHOICE_1_PHONE_RESET.start()
                        Day2Choice1Option.IGNORE -> Day2Stage.CHOICE_1_PHONE_IGNORE.start()
                    }
                }
            }
        }
    }

    fun onClickNextStage() {
        if (_state.value.isEndStage) {
            _state.value = _state.value.copy(isEndStage = false)
            when (_state.value.stage) {
                Day2Stage.CHOICE_1_PHONE_ANSWER_2 -> Day2Stage.END.start()
                Day2Stage.CHOICE_1_PHONE_RESET -> Day2Stage.END.start()
                else -> {
                    val nextStage = Day2Stage.entries.find { it.ordinal == _state.value.stage.ordinal + 1 } ?: Day2Stage.END
                    nextStage.start()
                }
            }
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
    private fun Day2Stage.start() {
        jobStage?.cancel()
        jobStage = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (this@start) {
                    Day2Stage.START -> {
                        _state.value = _state.value.copy(
                            stage = this@start,
                            isNextButtonEnable = false,
                        )
                        delay(1.seconds)
                        Day2Stage.RISE.start()
                    }

                    Day2Stage.RISE -> {
                        delay(1.seconds)
                        _state.value = _state.value.copy(
                            stage = this@start,
                            isNextButtonEnable = false,
                        )
                        delay(1.seconds)
                        audioPlayer = AudioPlayer(Res.readBytes("files/scene2_hrap.wav"))
                        audioPlayer?.play(true)
                        delay(4.seconds)
                        audioPlayer?.release()
                        audioPlayer = AudioPlayer(Res.readBytes("files/scene2_music1.wav"))
                        audioPlayer?.play()
                        delay(3.seconds)
                        _state.value = _state.value.copy(isNextButtonEnable = true)
                    }

                    Day2Stage.CHOICE_1_PHONE -> {
                        _state.value = _state.value.copy(
                            stage = this@start,
                            isNextButtonEnable = false
                        )
                        _state.first { it.isEndStage }
                        _state.value = _state.value.copy(choice = Day2Choice1)
                    }

                    Day2Stage.CHOICE_1_PHONE_ANSWER_1 -> {
                        audioPlayer?.release()
                        _state.value = _state.value.copy(
                            stage = this@start,
                            isNextButtonEnable = true
                        )
                    }

                    Day2Stage.CHOICE_1_PHONE_ANSWER_2 -> {
                        _state.value = _state.value.copy(stage = this@start)
                    }

                    Day2Stage.CHOICE_1_PHONE_RESET -> {
                        audioPlayer?.release()
                        _state.value = _state.value.copy(
                            stage = this@start,
                            isNextButtonEnable = true
                        )
                    }

                    Day2Stage.CHOICE_1_PHONE_IGNORE -> {
                        _state.value = _state.value.copy(
                            stage = this@start,
                            isNextButtonEnable = true
                        )
                    }

                    Day2Stage.END -> {
                        audioPlayer?.release()
                        _state.value = _state.value.copy(
                            stage = this@start,
                            isNextButtonEnable = false
                        )
                        delay(2.seconds)
                        engineOps.finishScene()
                    }

                }
            }
        }
    }
}