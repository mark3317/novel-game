package ru.markn.content.pres.day2

import ru.markn.engine.model.GameChoice

data class Day2UIState(
    val stage: Day2Stage = Day2Stage.START,
    val isEndStage: Boolean = true,
    val isNextButtonEnable: Boolean = false,
    val choice: GameChoice? = null,
)