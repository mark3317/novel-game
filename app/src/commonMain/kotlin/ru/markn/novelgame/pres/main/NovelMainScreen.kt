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
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import novel_game.app.generated.resources.Res
import novel_game.app.generated.resources.compose_multiplatform
import novel_game.app.generated.resources.main
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ru.markn.engine.audio.AudioPlayer
import ru.markn.engine.audio.PlayerState
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalResourceApi::class)
@Composable
fun INovelMainActions.NovelMainScreen(state: NovelMainUIState) {
    val audioPlayer = AudioPlayer()
    LaunchedEffect(Unit) {
        audioPlayer.stateFlow.collect { state ->
            if (state == PlayerState.IDLE) {
                audioPlayer.play(Res.readBytes("files/main.mp3"))
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.stop(durationFadeOut = 1.seconds)
        }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val windowWidth = maxWidth
        Image(
            painter = painterResource(Res.drawable.main),
            contentDescription = "background1",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        GameTitle()

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(windowWidth * 0.30f)
                .padding(start = windowWidth * 0.05f),
            verticalArrangement = Arrangement.Center
        ) {
            GameButton(
                onClick = ::startGame,
                text = "Начать игру"
            )
            GameButton(
                onClick = ::onClickShowButton,
                text = "Настройки"
            )
            GameButton(
                onClick = ::onClickExitButton,
                text = "Выход"
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(windowWidth * 0.20f),
            visible = state.isShowedText,
            enter = fadeIn() + slideInHorizontally { 2 * it },
            exit = fadeOut() + slideOutHorizontally { 2 * it },
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = "Compose Icon",
                )
                Text(
                    text = state.text,
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

@Composable
fun BoxWithConstraintsScope.GameTitle() {
    var text by remember { mutableStateOf("Все, что было до...") }
    var isClicked by remember { mutableStateOf(false) }

    val horizontalPadding = maxWidth * 0.2f
    val verticalPadding = maxHeight * 0.1f

    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = verticalPadding, start = horizontalPadding, end = horizontalPadding)
            .height(verticalPadding)
            .clickable(
                interactionSource = null,
                indication = null,
            ) {
                isClicked = !isClicked
                text = if (isClicked) "Все, что было до - это полная хуйня" else "Все, что было до..."
            }
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            }
        ) { targetText ->
            BasicText(
                text = targetText,
                autoSize = TextAutoSize.StepBased(minFontSize = 10.sp, maxFontSize = 96.sp, stepSize = 2.sp),
                style = TextStyle(
                    fontFamily = FontFamily.Cursive,
                    color = Color.White,
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
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicText(
            modifier = Modifier.padding(start = animatedPadding, top = 4.dp, bottom = 4.dp),
            maxLines = 1,
            text = text,
            autoSize = TextAutoSize.StepBased(minFontSize = 10.sp, maxFontSize = 42.sp, stepSize = 1.sp),
            style = TextStyle(
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