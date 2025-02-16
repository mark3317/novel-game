package ru.markn.content.pres.day2

import ru.markn.engine.model.ChoiceOption
import ru.markn.engine.model.GameChoice

enum class Day2Choice2Option(override val text: String) : ChoiceOption {
    YES("Да"),
    NO("Нет"),
}

object Day2Choice2 : GameChoice {
    override val options: List<ChoiceOption> = Day2Choice1Option.entries
}