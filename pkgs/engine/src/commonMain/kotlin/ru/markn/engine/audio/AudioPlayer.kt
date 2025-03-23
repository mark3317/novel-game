package ru.markn.engine.audio

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

expect class AudioPlayer() {
    val stateFlow: StateFlow<PlayerState>
    val volumeFlow: StateFlow<Float>
    fun play(url: String)
    fun play(bytes: ByteArray)
    fun setVolume(volume: Float)
    fun pause()
    fun resume()
    fun stop(durationFadeOut: Duration = Duration.ZERO)
    fun close()
}
