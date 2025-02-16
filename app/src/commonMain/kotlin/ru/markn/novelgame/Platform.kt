package ru.markn.novelgame

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform