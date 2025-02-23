package ru.markn.content.pres.scene1

import org.jetbrains.compose.resources.DrawableResource
import ru.markn.engine.mvi.IMviState

data class Scene1UIState(
    val phrase: String = "",
    val backImg: DrawableResource? = null,
    val isReadyToContinue: Boolean = false,
    val isAvailableNextButton: Boolean = false,
) : IMviState
