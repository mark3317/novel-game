package ru.markn.novelgame

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.defaultModule
import ru.markn.content.contentModule
import ru.markn.engine.engineModule
import ru.markn.engine.resources.ComposeApplication
import ru.markn.engine.resources.ComposeNavigation
import ru.markn.novelgame.pres.novelGameGraph

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameApp(koinAppDeclaration: KoinAppDeclaration? = null) = ComposeApplication({
    koinAppDeclaration?.invoke(this)
    defaultModule()
    engineModule()
    contentModule()
}) {
    ComposeNavigation {
        novelGameGraph()
    }
}