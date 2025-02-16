package ru.markn.novelgame.pres.game

import ru.markn.novelgame.domain.Scene

data class NovelGameUIState(
    val scene: Scene = Scene.entries.first(),
    val isFinishedGame: Boolean = false,
)