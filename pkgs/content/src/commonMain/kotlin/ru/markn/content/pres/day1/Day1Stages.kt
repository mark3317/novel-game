package ru.markn.content.pres.day1

import novel_game.pkgs.content.generated.resources.*
import novel_game.pkgs.content.generated.resources.Res
import novel_game.pkgs.content.generated.resources.scene1_back1
import novel_game.pkgs.content.generated.resources.scene1_back2
import novel_game.pkgs.content.generated.resources.scene1_back3
import org.jetbrains.compose.resources.DrawableResource

enum class Day1Stages(val phrase: Day1Phrases, val backImg: DrawableResource?) {
    START(Day1Phrases.EMPTY, null),
    OCTOBER(Day1Phrases.OCTOBER, Res.drawable.scene1_back1),
    THIS_CITY_AFRAID_OF_ME(Day1Phrases.THIS_CITY_AFRAID_OF_ME, Res.drawable.scene1_back2),
    BELLOW_ME_THIS_CITY(Day1Phrases.BELLOW_ME_THIS_CITY, Res.drawable.scene1_back3),
    WHOLE_WORLD(Day1Phrases.WHOLE_WORLD, Res.drawable.scene1_back4),
    END(Day1Phrases.EMPTY, null)
}