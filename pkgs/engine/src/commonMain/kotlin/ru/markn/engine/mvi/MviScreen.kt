package ru.markn.engine.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
inline fun <reified VM, A : IMviActions, S : IMviState> MviScreen(
    vm: VM,
    screen: @Composable A.(S) -> Unit
) where VM : MviViewModel<S>, VM : A {
    val state by vm.stateFlow.collectAsStateWithLifecycle(vm.state)
    vm.screen(state)
}