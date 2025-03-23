package ru.markn.content.pres.scene1

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import novel_game.pkgs.content.generated.resources.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.annotation.KoinViewModel
import ru.markn.content.domain.Scene2
import ru.markn.engine.audio.AudioPlayer
import ru.markn.engine.model.GamePartScript
import ru.markn.engine.mvi.MviViewModel
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class Scene1Processor(
    private val navController: NavController
) : IScene1Actions, MviViewModel<Scene1UIState>(
    Scene1UIState()
) {
    private val scenePartFlow = MutableStateFlow(Part.INTRO)
    private val continueFlow = MutableSharedFlow<Unit>()
    private var audioPlayer = AudioPlayer()

    private enum class Part {
        INTRO
    }

    override val observableFlows: List<Flow<*>>
        get() = listOf(
            scenePartFlow.onEach {
                sceneParts[it]?.start()
            }
        )

    @OptIn(ExperimentalResourceApi::class)
    private val sceneParts: Map<Part, GamePartScript> = mapOf(
        Part.INTRO to GamePartScript {
            audioPlayer.play(Res.readBytes("files/scene1_music1.mp3"))
            delay(2.seconds)
            updateState {
                copy(
                    phrase = "12 октября 2025-го.",
                    backImg = Res.drawable.scene1_back1,
                    isReadyToContinue = false,
                    isAvailableNextButton = true
                )
            }
            continueFlow.first()
            updateState {
                copy(
                    phrase = "Этот город боится меня, я видел его истинное лицо: Все эти либералы, интеллектуалы, сладкоголосые болтуны... и от чего-то вдруг ни кто не знает что сказать.",
                    backImg = Res.drawable.scene1_back2,
                    isReadyToContinue = false
                )
            }
            continueFlow.first()
            updateState {
                copy(
                    phrase = "Подо мной, этот ужасный город, он вопит как скотобойня полная умственно-отсталых детей, а ночь воняет блудом и нечистой совестью.",
                    backImg = Res.drawable.scene1_back3,
                    isReadyToContinue = false
                )
            }
            continueFlow.first()
            updateState {
                copy(
                    phrase = "Теперь весь мир стоит на краю, глядя в чертово пекло. А эта ночь воняет блудом и нечистой совестью.",
                    backImg = Res.drawable.scene1_back4,
                    isReadyToContinue = false
                )
            }
            continueFlow.first()
            audioPlayer.stop(durationFadeOut = 1.seconds)
            withContext(Dispatchers.Main) {
                navController.navigate(Scene2) {
                    popUpTo(0)
                }
            }
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

    override fun disposeScreen() {
        audioPlayer.stop()
    }
}