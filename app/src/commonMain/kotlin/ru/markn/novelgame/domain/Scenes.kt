package ru.markn.novelgame.domain

import androidx.compose.runtime.Composable
import ru.markn.content.pres.day1.Day1Screen
import ru.markn.engine.model.GameScene

enum class Scenes : GameScene {
    DAY1 {
        @Composable
        override fun show() = Day1Screen()
    },
}