package ru.markn.novelgame.pres.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.annotation.KoinViewModel
import ru.markn.engine.GameEngineOps
import ru.markn.engine.mvi.MviViewModel
import ru.markn.engine.utils.exitProgram
import ru.markn.engine.utils.getPlatform
import ru.markn.novelgame.domain.Game

@OptIn(ExperimentalResourceApi::class)
@KoinViewModel
class NovelMainProcessor(
    private val ops: GameEngineOps
) : INovelMainActions, MviViewModel<NovelMainUIState>(
    NovelMainUIState(
        text = "Hello Compose: ${getPlatform()}"
    )
) {
    override val observableFlows: List<Flow<*>>
        get() = listOf(
            ops.isFinishedGameFlow.onEach {
                updateState {
                    copy(text = "Game over")
                }
            }
        )

    override fun onClickShowButton() = intent(
        name = "onClickShowButton",
        updateStateImmediate = {
            copy(isShowedText = !isShowedText)
        },
    )

    override fun startGame() = skipDebounceIntent("startGame") {
        navigateTo(Game)
    }

    override fun onClickExitButton() = skipDebounceIntent("onClickExitButton") {
        exitProgram()
    }
}