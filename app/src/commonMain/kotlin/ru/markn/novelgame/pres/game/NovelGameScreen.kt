package ru.markn.novelgame.pres.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import ru.markn.content.pres.gameGraph

@Composable
fun INovelGameActions.NovelGameScreen(state: NovelGameUIState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        gameGraph()
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopCenter),
            text = state.header,
            color = Color.White,
            maxLines = 1
        )
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 60.dp)
                .align(Alignment.TopStart)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
            ) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, size.height)
                    lineTo(size.width * 0.7f, size.height)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path, color = Color.Gray)
            }
            IconButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(8.dp)
                    .align(Alignment.TopStart),
                onClick = ::backToMain
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    imageVector = Icons.Default.Home,
                    contentDescription = "Домой",
                    tint = Color.White
                )
            }
        }
    }
}
