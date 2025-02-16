package ru.markn.content.pres.day2

import novel_game.pkgs.content.generated.resources.*
import novel_game.pkgs.content.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource

enum class Day2Stage(val phrase: Day2Phrase, val backImg: DrawableResource?) {
    START(Day2Phrase.EMPTY, null),
    RISE(Day2Phrase.EMPTY, Res.drawable.scene2_back1),
    CHOICE_1_PHONE(Day2Phrase.ANSWER_PHONE, Res.drawable.scene2_back2),

    CHOICE_1_PHONE_ANSWER_1(Day2Phrase.MAKS_HI_LESHA, Res.drawable.scene2_back3),
    CHOICE_1_PHONE_ANSWER_2(Day2Phrase.LESHA_YES_I_RUN, Res.drawable.scene2_back3),

    CHOICE_1_PHONE_RESET(Day2Phrase.LESHA_I_MISS, Res.drawable.scene2_back1),

    CHOICE_1_PHONE_IGNORE(Day2Phrase.LESHA_BETTER_NOT_ANSWER, Res.drawable.scene2_back1),

    END(Day2Phrase.EMPTY, null)
}