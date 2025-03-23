package ru.markn.novelgame.pres.main

import ru.markn.engine.mvi.IMviActions

interface INovelMainActions : IMviActions {
    fun onClickShowButton()
    fun startGame()
    fun onClickExitButton()
}