package ru.markn.novelgame.pres.game

import ru.markn.engine.mvi.IMviActions

interface INovelGameActions : IMviActions {
    fun backToMain()
}