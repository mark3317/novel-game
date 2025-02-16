package ru.markn.content.pres.day1

data class Day1UIState(
    val stage: Day1Stage = Day1Stage.START,
    val isEndStage: Boolean = false
)