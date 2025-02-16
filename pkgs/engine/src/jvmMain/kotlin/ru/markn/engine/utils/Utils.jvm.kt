package ru.markn.engine.utils

import ru.markn.engine.model.Platform
import kotlin.system.exitProcess

actual fun exitProgram() {
    exitProcess(0)
}

actual fun getPlatform(): Platform = Platform.JVM