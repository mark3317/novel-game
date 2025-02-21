package ru.markn.novelgame.pres.main

import ru.markn.engine.mvi.IMviState

data class NovelMainUIState(
    val isShowedText: Boolean = false,
    val text: String = ""
) : IMviState