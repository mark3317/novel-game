package ru.markn.engine.resources

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import ru.markn.engine.mvi.MviNavEvent
import ru.markn.engine.mvi.MviViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ComposeApplication(
    koinAppDeclaration: KoinAppDeclaration? = null,
    content: @Composable context(NavHostController, SharedTransitionScope) () -> Unit,
) = KoinApplication({
    koinAppDeclaration?.invoke(this)
}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        val navController: NavHostController = rememberNavController()
        SharedTransitionLayout {
            MaterialTheme {
                navController.apply {
                    content()
                }
            }
        }
        LaunchedEffect(Unit) {
            MviViewModel.eventsNavFlow.collect { navEvent ->
                when (navEvent) {
                    MviNavEvent.NavigateBack -> navController.popBackStack()
                    MviNavEvent.NavigateUp -> navController.navigateUp()
                    is MviNavEvent.NavigateBackTo ->
                        navController.popBackStack(
                            route = navEvent.popUpTo,
                            inclusive = navEvent.inclusive,
                        )

                    is MviNavEvent.NavigateTo ->
                        navController.navigate(
                            route = navEvent.route,
                        )
                }
            }
        }
    }
}

context(sharedTransitionScope: SharedTransitionScope, navHostController: NavHostController)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ComposeNavigation(graphBuilder: SharedTransitionScope.() -> NavGraph) {
    NavHost(
        navController = navHostController,
        graph = remember(graphBuilder) {
            graphBuilder(sharedTransitionScope)
        },
    )
}
