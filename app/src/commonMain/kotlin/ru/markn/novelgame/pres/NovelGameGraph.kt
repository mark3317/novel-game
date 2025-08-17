package ru.markn.novelgame.pres

import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import org.koin.compose.viewmodel.koinViewModel
import ru.markn.engine.mvi.MviScreen
import ru.markn.novelgame.domain.Game
import ru.markn.novelgame.domain.Main
import ru.markn.novelgame.pres.game.NovelGameProcessor
import ru.markn.novelgame.pres.game.NovelGameScreen
import ru.markn.novelgame.pres.main.NovelMainProcessor
import ru.markn.novelgame.pres.main.NovelMainScreen

context(navHostController: NavHostController)
fun novelGameGraph() = navHostController.createGraph(startDestination = Main) {
    composable<Main> {
        MviScreen(vm = koinViewModel<NovelMainProcessor>()) {
            NovelMainScreen(it)
        }
    }
    composable<Game> {
        MviScreen(vm = koinViewModel<NovelGameProcessor>()) {
            NovelGameScreen(it)
        }
    }
}
