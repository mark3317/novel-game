package ru.markn.content.pres.scene2

import org.jetbrains.compose.resources.DrawableResource
import ru.markn.engine.model.GameChoice
import ru.markn.engine.mvi.IMviState

data class Scene2UIState(
    val titleScene: String = "",
    val phrase: String = "",
    val gameChoice: GameChoice? = null,
    val backImg: DrawableResource? = null,
    val isReadyToContinue: Boolean = false,
    val isAvailableNextButton: Boolean = false,
) : IMviState
