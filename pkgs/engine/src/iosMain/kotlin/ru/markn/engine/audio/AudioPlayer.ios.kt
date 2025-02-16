package ru.markn.engine.audio

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.Foundation.NSURL

actual class AudioPlayer actual constructor(
    bytes: ByteArray
) {
    private var player: AVPlayer? = null

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun play(url: String) {
        release()
        AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback, null)
        val nsUrl = NSURL(string = url)
        player = AVPlayer.playerWithURL(nsUrl)
        AVAudioPlayer()
        player?.play()
    }

    actual suspend fun play(isRepeated: Boolean) {
    }

    actual suspend fun release() {
        player?.pause()
        player = null
    }

    actual suspend fun stop() {
        player?.pause()
    }
}