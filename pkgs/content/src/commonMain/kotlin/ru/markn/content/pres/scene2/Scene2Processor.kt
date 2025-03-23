package ru.markn.content.pres.scene2

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import novel_game.pkgs.content.generated.resources.*
import novel_game.pkgs.content.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.annotation.KoinViewModel
import ru.markn.content.domain.models.scene2.Scene2Choice1
import ru.markn.engine.GameEngineOps
import ru.markn.engine.audio.AudioPlayer
import ru.markn.engine.audio.PlayerState
import ru.markn.engine.model.GameChoiceOption
import ru.markn.engine.model.GamePartScript
import ru.markn.engine.mvi.MviViewModel
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class Scene2Processor(
    private val ops: GameEngineOps
) : IScene2Actions, MviViewModel<Scene2UIState>(
    Scene2UIState()
) {
    private val scenePartFlow = MutableStateFlow(Part.WAKE_UP)
    private val choiceOptionFlow = MutableSharedFlow<GameChoiceOption>()
    private val continueFlow = MutableSharedFlow<Unit>()
    private var audioPlayer: AudioPlayer = AudioPlayer()

    private enum class Part {
        WAKE_UP,
        CHOICE_1_ANSWER,
        CHOICE_1_RESET,
        CHOICE_1_IGNORE,
        FINISHED,
    }

    override val observableFlows: List<Flow<*>>
        get() = listOf(
            scenePartFlow.onEach {
                sceneParts[it]?.start()
            }
        )

    @OptIn(ExperimentalResourceApi::class)
    private val sceneParts: Map<Part, GamePartScript> = mapOf(
        Part.WAKE_UP to GamePartScript {
            delay(1.seconds)
            updateState {
                copy(
                    titleScene = "День 1",
                )
            }
            delay(1.seconds)
            updateState {
                copy(
                    titleScene = "",
                    backImg = Res.drawable.scene2_back1,
                )
            }
            delay(1.seconds)
            audioPlayer.play(Res.readBytes("files/scene2_hrap.mp3"))
            delay(1.seconds)
            audioPlayer.stateFlow.first { it == PlayerState.IDLE }
            audioPlayer.play(Res.readBytes("files/scene2_music1.mp3"))
            updateState {
                copy(
                    isReadyToContinue = true,
                    isAvailableNextButton = true,
                )
            }
            continueFlow.first()
            updateState {
                copy(
                    phrase = "Лешу разбудил звонок от его друга Макса, заядлого спортсмена, который не пропускал ни дня без тренировок. Ему совсем не хотелось вставать, но данное обещание пойти сегодня на утреннюю пробежку ставило его перед непростым выбором.",
                    backImg = Res.drawable.scene2_back2,
                    isReadyToContinue = false,
                )
            }
            stateFlow.first { it.isReadyToContinue }
            updateState {
                copy(
                    gameChoice = Scene2Choice1,
                    isReadyToContinue = true,
                    isAvailableNextButton = false
                )
            }
            choiceOptionFlow.first { it in Scene2Choice1.options }.let {
                when (it) {
                    Scene2Choice1.ANSWER -> scenePartFlow.value = Part.CHOICE_1_ANSWER
                    Scene2Choice1.RESET -> scenePartFlow.value = Part.CHOICE_1_RESET
                    Scene2Choice1.IGNORE -> scenePartFlow.value = Part.CHOICE_1_IGNORE
                }
            }
        },
        Part.CHOICE_1_ANSWER to GamePartScript {
            audioPlayer.stop()
            updateState {
                copy(
                    phrase = "Макс: Привет, Леша! Ты что спишь?! Давай вставай и идем на тренировку!",
                    backImg = Res.drawable.scene2_back3,
                    isAvailableNextButton = true,
                    isReadyToContinue = false,
                    gameChoice = null,
                )
            }
            continueFlow.first()
            updateState {
                copy(
                    phrase = "Леша: Да, уже бегу!",
                    isReadyToContinue = false,
                )
            }
            continueFlow.first()
            scenePartFlow.value = Part.FINISHED
        },
        Part.CHOICE_1_RESET to GamePartScript {
            audioPlayer.stop()
            updateState {
                copy(
                    phrase = "Леша: Ничего страшного если я пропущу одну тренировку. Тем более я не выспался, а полноценный сон ничуть не хуже тренировки для здоровья.",
                    backImg = Res.drawable.scene2_back1,
                    isAvailableNextButton = true,
                    isReadyToContinue = false,
                    gameChoice = null,
                )
            }
            continueFlow.first()
            scenePartFlow.value = Part.FINISHED
        },
        Part.CHOICE_1_IGNORE to GamePartScript {
            updateState {
                copy(
                    phrase = "Леша: А если просто не взять трубку? Макс подумает, что я еще сплю... В конце концов, это не преступление – проспать тренировку.",
                    backImg = Res.drawable.scene2_back1,
                    isAvailableNextButton = true,
                    isReadyToContinue = false,
                    gameChoice = null,
                )
            }
            continueFlow.first()
            audioPlayer.stop()
            scenePartFlow.value = Part.FINISHED
        },
        Part.FINISHED to GamePartScript {
            updateState {
                copy(
                    titleScene = "В разработке",
                    phrase = "",
                    backImg = null,
                    isAvailableNextButton = false,
                    isReadyToContinue = false,
                )
            }
            delay(2.seconds)
            ops.isFinishedGameFlow.emit(Unit)
        }
    )

    override fun prepareContinue() {
        updateState {
            copy(isReadyToContinue = true)
        }
    }

    override fun onClickNextButton() {
        viewModelScope.launch {
            if (state.isReadyToContinue) {
                continueFlow.emit(Unit)
            } else {
                prepareContinue()
            }
        }
    }

    override fun onClickChoiceOption(option: GameChoiceOption) {
        viewModelScope.launch {
            choiceOptionFlow.emit(option)
        }
    }

    override fun disposeScreen() {
        audioPlayer.stop()
    }
}