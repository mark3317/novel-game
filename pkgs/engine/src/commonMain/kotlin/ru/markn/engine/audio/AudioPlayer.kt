package ru.markn.engine.audio

expect class AudioPlayer(
    bytes: ByteArray
) {
    suspend fun play(url: String)
    suspend fun play(isRepeated: Boolean = false)
    suspend fun stop()
    suspend fun release()
}