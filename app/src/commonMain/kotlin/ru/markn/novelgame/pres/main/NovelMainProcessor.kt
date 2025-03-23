package ru.markn.novelgame.pres.main

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    private val navController: NavController,
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

    override fun onClickShowButton() {
        updateState {
            copy(isShowedText = !isShowedText)
        }
    }

    override fun startGame() {
        viewModelScope.launch {
            navController.navigate(Game)
        }
    }

    override fun onClickExitButton() {
        exitProgram()
    }
}