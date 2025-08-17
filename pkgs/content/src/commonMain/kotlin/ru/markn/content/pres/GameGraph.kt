package ru.markn.content.pres

import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import org.koin.compose.viewmodel.koinViewModel
import ru.markn.content.domain.Scene1
import ru.markn.content.domain.Scene2
import ru.markn.content.pres.scene1.Scene1Processor
import ru.markn.content.pres.scene1.Scene1Screen
import ru.markn.content.pres.scene2.Scene2Processor
import ru.markn.content.pres.scene2.Scene2Screen
import ru.markn.engine.mvi.MviScreen

context(navHostController: NavHostController)
fun gameGraph() = navHostController.createGraph(startDestination = Scene1) {
    composable<Scene1> {
        MviScreen(vm = koinViewModel<Scene1Processor>()) {
            Scene1Screen(it)
        }
    }
    composable<Scene2> {
        MviScreen(vm = koinViewModel<Scene2Processor>()) {
            Scene2Screen(it)
        }
    }
}
