package ru.markn.novelgame.pres

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.markn.engine.mvi.MviScreen
import ru.markn.novelgame.domain.Game
import ru.markn.novelgame.domain.Main
import ru.markn.novelgame.pres.game.NovelGameProcessor
import ru.markn.novelgame.pres.game.NovelGameScreen
import ru.markn.novelgame.pres.main.NovelMainProcessor
import ru.markn.novelgame.pres.main.NovelMainScreen

@Preview
@Composable
fun novelGameGraph() {
    val navController = rememberNavController()
    MaterialTheme {
        NavHost(navController, startDestination = Main) {
            composable<Main> {
                MviScreen(vm = koinViewModel<NovelMainProcessor> { parametersOf(navController) }) {
                    NovelMainScreen(it)
                }
            }
            composable<Game> {
                MviScreen(vm = koinViewModel<NovelGameProcessor> { parametersOf(navController) }) {
                    NovelGameScreen(it)
                }
            }
        }
    }
}