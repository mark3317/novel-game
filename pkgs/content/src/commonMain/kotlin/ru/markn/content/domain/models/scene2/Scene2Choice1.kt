package ru.markn.content.domain.models.scene2

import ru.markn.engine.model.GameChoice
import ru.markn.engine.model.GameChoiceOption

object Scene2Choice1 : GameChoice {
    override val options: List<GameChoiceOption> = listOf(
        ANSWER,
        RESET,
        IGNORE
    )

    object ANSWER : GameChoiceOption {
        override val text: String = "Ответить"
    }

    object RESET : GameChoiceOption {
        override val text: String = "Сбросить"
    }

    object IGNORE : GameChoiceOption {
        override val text: String = "Проигнорировать"
    }
}
