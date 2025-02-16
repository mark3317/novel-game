package ru.markn.novelgame.domain

import androidx.compose.runtime.Composable
import ru.markn.content.pres.day1.Day1Screen
import ru.markn.content.pres.day2.Day2Screen
import ru.markn.engine.model.GameScene

enum class Scene : GameScene {
    DAY1 {
        @Composable
        override fun show() = Day1Screen()
    },

    DAY2 {
        @Composable
        override fun show() = Day2Screen()
    },
}