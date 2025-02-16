package ru.markn.engine.model

import androidx.compose.runtime.Composable

fun interface GameScene {
    @Composable
    fun show()
}