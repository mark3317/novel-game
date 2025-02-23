package ru.markn.content.pres

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.markn.content.domain.Scene1
import ru.markn.content.domain.Scene2
import ru.markn.content.pres.scene1.Scene1Processor
import ru.markn.content.pres.scene1.Scene1Screen
import ru.markn.content.pres.scene2.Scene2Processor
import ru.markn.content.pres.scene2.Scene2Screen
import ru.markn.engine.mvi.MviScreen

@Composable
fun gameGraph() {
    val navController = rememberNavController()
    MaterialTheme {
        NavHost(navController, startDestination = Scene1) {
            composable<Scene1> {
                MviScreen(vm = koinViewModel<Scene1Processor> { parametersOf(navController) }) {
                    Scene1Screen(it)
                }
            }
            composable<Scene2> {
                MviScreen(vm = koinViewModel<Scene2Processor>()) {
                    Scene2Screen(it)
                }
            }
        }
    }
}