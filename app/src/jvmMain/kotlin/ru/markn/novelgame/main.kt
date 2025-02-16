package ru.markn.novelgame

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() = application {
    Window(
        state = WindowState(
            placement = WindowPlacement.Fullscreen,
        ),
        onCloseRequest = ::exitApplication,
        title = "Novel Game",
    ) {
        window.minimumSize = Dimension(800, 600)
        GameApp()
    }
}
