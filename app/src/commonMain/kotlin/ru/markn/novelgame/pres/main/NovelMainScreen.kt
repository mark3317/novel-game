package ru.markn.novelgame.pres.main

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import novel_game.app.generated.resources.Res
import novel_game.app.generated.resources.back1
import novel_game.app.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import ru.markn.novelgame.domain.Game

@Composable
fun NovelMainScreen(navController: NavController) {
    val vm = koinViewModel<NovelMainProcessor>()
    val state by vm.state.collectAsStateWithLifecycle()

    val windowSize = remember {
        mutableStateOf(IntSize(0, 0))
    }

    LaunchedEffect(Unit) {
        vm.playMusic()
    }

    Box(
        modifier = Modifier.fillMaxSize().onSizeChanged {
            windowSize.value = it
        }
    ) {
        Image(
            painter = painterResource(Res.drawable.back1),
            contentDescription = "background1",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        GameTitle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 120.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                GameButton(
                    onClick = {
                        navController.navigate(Game)
                        vm.startGame()
                    },
                    text = "Начать игру"
                )
                GameButton(
                    onClick = vm::onClickShowButton,
                    text = "Настройки"
                )
                GameButton(
                    onClick = vm::onClickExitButton,
                    text = "Выход"
                )
            }

            AnimatedVisibility(
                visible = state.isShowedText,
                enter = fadeIn() + slideInHorizontally { 2 * it },
                exit = fadeOut() + slideOutHorizontally { 2 * it },
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(200.dp),
                        painter = painterResource(Res.drawable.compose_multiplatform),
                        contentDescription = "Compose Icon",
                    )
                    Text(
                        text = "Compose: ${state.text}",
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                blurRadius = 3f,
                                offset = Offset(2f, 2f)
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun GameTitle(
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("Все, что было до...") }
    var isClicked by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
            ) {
                isClicked = !isClicked
                text = if (isClicked) "Все, что было до - это полная хуйня" else "Все, что было до..."
            },
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            }
        ) { targetText ->
            Text(
                text = targetText,
                style = TextStyle(
                    fontSize = 96.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Cursive,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 3f,
                        offset = Offset(2f, 2f)
                    )
                ),
            )
        }
    }
}

@Composable
fun GameButton(
    onClick: () -> Unit,
    text: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isHoverEnter by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            isHoverEnter = when (interaction) {
                is HoverInteraction.Enter -> true
                is HoverInteraction.Exit -> false
                else -> isHoverEnter
            }
        }
    }
    val animatedPadding by animateDpAsState(
        targetValue = if (isHoverEnter) 16.dp else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .then(
                if (isHoverEnter) Modifier.background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.DarkGray.copy(alpha = 0.3f),
                            Color.DarkGray.copy(alpha = 0.6f),
                            Color.DarkGray.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                ) else Modifier
            )
    ) {
        Text(
            modifier = Modifier.padding(start = animatedPadding, top = 4.dp, bottom = 4.dp),
            text = text,
            style = TextStyle(
                fontSize = 42.sp,
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black,
                    blurRadius = 3f,
                    offset = Offset(2f, 2f)
                )
            )
        )
    }
}