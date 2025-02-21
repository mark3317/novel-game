package ru.markn.novelgame.pres.game

import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel
import ru.markn.engine.mvi.MviViewModel
import ru.markn.novelgame.domain.NovelGameOps

@KoinViewModel
class NovelGameProcessor(
    private val navController: NavController,
    private val gameOps: NovelGameOps
) : INovelGameActions, MviViewModel<NovelGameUIState>(
    NovelGameUIState()
) {

    override val observableFlows: List<Flow<*>>
        get() = listOf(
            gameOps.currentSceneFlow.onEach { scene ->
                updateState {
                    copy(scene = scene)
                }
            },
            gameOps.isFinishedGameFlow.onEach { isFinished ->
                if (isFinished) {
                    navController.popBackStack()
                }
            }
        )

    override fun backToMain() {
        navController.popBackStack()
    }
}