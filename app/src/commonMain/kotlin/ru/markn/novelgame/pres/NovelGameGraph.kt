package ru.markn.novelgame.pres

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.markn.novelgame.domain.Game
import ru.markn.novelgame.domain.Main
import ru.markn.novelgame.pres.game.NovelGameScreen
import ru.markn.novelgame.pres.main.NovelMainScreen

@Preview
@Composable
fun novelGameGraph() {
    val navController = rememberNavController()
    MaterialTheme {
        NavHost(navController, startDestination = Main) {
            composable<Main> {
                NovelMainScreen(navController = navController)
            }
            composable<Game> {
                NovelGameScreen(navController = navController)
            }
        }
    }
}