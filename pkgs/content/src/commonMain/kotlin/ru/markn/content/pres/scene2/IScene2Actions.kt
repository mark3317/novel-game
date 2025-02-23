package ru.markn.content.pres.scene2

import ru.markn.engine.model.GameChoiceOption
import ru.markn.engine.mvi.IMviActions

interface IScene2Actions : IMviActions {
    fun prepareContinue()
    fun onClickNextButton()
    fun onClickChoiceOption(option: GameChoiceOption)
}