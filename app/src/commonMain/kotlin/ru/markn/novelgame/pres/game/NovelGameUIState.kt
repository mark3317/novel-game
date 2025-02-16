package ru.markn.novelgame.pres.game

import ru.markn.novelgame.domain.Scenes

data class NovelGameUIState(
    val scene: Scenes = Scenes.DAY1,
    val isFinishedGame: Boolean = false,
)