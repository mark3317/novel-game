package ru.markn.novelgame.pres.game

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel
import ru.markn.engine.GameEngineOps
import ru.markn.engine.mvi.MviViewModel

@KoinViewModel
class NovelGameProcessor(
    private val engineOps: GameEngineOps
) : INovelGameActions, MviViewModel<NovelGameUIState>(
    NovelGameUIState()
) {

    override val observableFlows: List<Flow<*>>
        get() = listOf(
            engineOps.isFinishedGameFlow.onEach {
                navigateBack()
            }
        )

    override fun backToMain() = skipDebounceIntent("backToMain") {
        navigateBack()
    }
}