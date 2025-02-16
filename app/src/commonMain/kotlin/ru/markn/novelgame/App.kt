package ru.markn.novelgame

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.defaultModule
import ru.markn.content.contentModule
import ru.markn.engine.engineModule
import ru.markn.novelgame.pres.novelGameGraph

@Preview
@Composable
fun GameApp(koinAppDeclaration: KoinAppDeclaration? = null) =
    KoinApplication({
        koinAppDeclaration?.invoke(this)
        defaultModule()
        engineModule()
        contentModule()
    }) {
        novelGameGraph()
    }