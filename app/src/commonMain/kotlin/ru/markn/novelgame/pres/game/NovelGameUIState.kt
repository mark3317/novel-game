package ru.markn.novelgame.pres.game

import ru.markn.engine.mvi.IMviState

data class NovelGameUIState(
    val header: String = "novelgame-build:1.0.50223",
) : IMviState