package ru.markn.engine.audio

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.Foundation.NSURL
import kotlin.time.Duration

actual class AudioPlayer {
    private var player: AVPlayer? = null
    private val _volumeFlow = MutableStateFlow(0.5f)
    private val _stateFlow = MutableStateFlow(PlayerState.IDLE)

    actual val stateFlow: StateFlow<PlayerState>
        get() = _stateFlow.asStateFlow()

    actual val volumeFlow: StateFlow<Float>
        get() = _volumeFlow.asStateFlow()

    @OptIn(ExperimentalForeignApi::class)
    actual fun play(url: String) {
        AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback, null)
        val nsUrl = NSURL(string = url)
        player = AVPlayer.playerWithURL(nsUrl)
        AVAudioPlayer()
        player?.play()
    }

    actual fun play(bytes: ByteArray) {}
    actual fun setVolume(volume: Float) {}
    actual fun pause() {}
    actual fun resume() {}
    actual fun stop(durationFadeOut: Duration) {
        player?.pause()
    }
    actual fun close() {}
}