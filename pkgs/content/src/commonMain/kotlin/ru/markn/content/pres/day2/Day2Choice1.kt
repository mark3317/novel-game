package ru.markn.content.pres.day2

import ru.markn.engine.model.ChoiceOption
import ru.markn.engine.model.GameChoice


enum class Day2Choice1Option(override val text: String) : ChoiceOption {
    ANSWER("Ответить"),
    RESET("Сбросить"),
    IGNORE("Проигнорировать"),
}

object Day2Choice1 : GameChoice {
    override val options: List<ChoiceOption> = Day2Choice1Option.entries
}
