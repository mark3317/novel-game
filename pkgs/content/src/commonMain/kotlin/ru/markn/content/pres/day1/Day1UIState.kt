package ru.markn.content.pres.day1

data class Day1UIState(
    val stage: Day1Stages = Day1Stages.START,
    val isEndStage: Boolean = false
)