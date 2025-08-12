package ru.markn.engine.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
inline fun <reified VM, A : IMviActions, S : IMviState> MviScreen(
    vm: VM,
    navController: NavController? = null,
    screen: @Composable A.(S) -> Unit
) where VM : MviViewModel<S>, VM : A {
    val state by vm.stateFlow.collectAsStateWithLifecycle(vm.state)
    val navDestination by vm.navigateStateFlow.collectAsStateWithLifecycle(null)
    LaunchedEffect(navDestination) {
        navDestination?.let {
            navController?.navigate(it)
        }
    }
    vm.screen(state)
}