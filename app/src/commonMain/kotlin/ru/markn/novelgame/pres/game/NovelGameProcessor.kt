package ru.markn.novelgame.pres.game

import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel
import ru.markn.engine.GameEngineOps
import ru.markn.engine.mvi.MviViewModel

@KoinViewModel
class NovelGameProcessor(
    private val navController: NavController,
    private val engineOps: GameEngineOps
) : INovelGameActions, MviViewModel<NovelGameUIState>(
    NovelGameUIState()
) {

    override val observableFlows: List<Flow<*>>
        get() = listOf(
            engineOps.isFinishedGameFlow.onEach {
                withContext(Dispatchers.Main) {
                    navController.popBackStack()
                }
            }
        )

    override fun backToMain() {
        navController.popBackStack()
    }
}