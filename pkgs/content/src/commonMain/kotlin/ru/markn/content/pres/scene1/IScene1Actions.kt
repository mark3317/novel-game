package ru.markn.content.pres.scene1

import ru.markn.engine.mvi.IMviActions

interface IScene1Actions : IMviActions {
    fun prepareContinue()
    fun onClickNextButton()
    fun disposeScreen()
}