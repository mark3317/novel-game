package ru.markn.content.pres.scene2

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun IScene2Actions.Scene2Screen(state: Scene2UIState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Crossfade(
            modifier = Modifier.matchParentSize(),
            targetState = state.backImg,
            animationSpec = tween(1000)
        ) { backImg ->
            backImg?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = "Background Day 2",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Crossfade(
            modifier = Modifier.align(Alignment.Center),
            targetState = state.titleScene,
            animationSpec = tween(1000)
        ) { title ->
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 52.sp,
                        color = Color.White
                    ),
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart),
            visible = state.gameChoice != null,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally(),
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                state.gameChoice?.options?.forEach { choiceOption ->
                    GameButton(
                        onClick = { onClickChoiceOption(choiceOption) },
                        text = choiceOption.text
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(8.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Crossfade(
                modifier = Modifier.weight(1f),
                targetState = state.phrase
            ) { text ->
                if (text.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.DarkGray.copy(alpha = 0.5f),
                                        Color.DarkGray.copy(alpha = 0.6f),
                                        Color.DarkGray.copy(alpha = 0.5f),
                                        Color.Transparent
                                    )
                                )
                            ),
                    ) {
                        if (!state.isReadyToContinue) {
                            AnimatedText(
                                text = text,
                                onEndingPhrase = ::prepareContinue
                            )
                        } else {
                            Text(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                text = text,
                                style = TextStyle(
                                    fontSize = 24.sp,
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
                } else {
                    Spacer(modifier = Modifier.fillMaxSize())
                }
            }
            AnimatedVisibility(
                visible = state.isAvailableNextButton,
                enter = slideInHorizontally { it * 2 },
                exit = slideOutHorizontally { it * 2 }
            ) {
                IconButton(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxHeight(),
                    onClick = ::onClickNextButton,
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedText(
    text: String,
    onEndingPhrase: () -> Unit,
    duration: Duration = 30.milliseconds
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        displayedText = ""
        text.forEachIndexed { index, _ ->
            displayedText = text.take(index + 1)
            delay(duration)
        }
    }

    if (displayedText == text) {
        onEndingPhrase()
    }

    Text(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        text = displayedText,
        style = TextStyle(
            fontSize = 24.sp,
            color = Color.White,
            shadow = Shadow(
                color = Color.Black,
                blurRadius = 3f,
                offset = Offset(2f, 2f)
            )
        ),
    )
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
                fontSize = 32.sp,
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