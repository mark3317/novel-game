package ru.markn.engine.audio

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent

actual class AudioPlayer actual constructor(
    bytes: ByteArray
) {
    private var clip: Clip = AudioSystem.getClip().apply {
        open(AudioSystem.getAudioInputStream(ByteArrayInputStream(bytes)))
    }

    actual suspend fun play(url: String) {

    }

    actual suspend fun play(isRepeated: Boolean) {
//        val ais = MpegAudioFileReader().getAudioInputStream(ByteArrayInputStream(bytes))
        withContext(Dispatchers.IO) {
            clip.start()
            if (isRepeated) {
                clip.addLineListener {
                    if (it.type == LineEvent.Type.STOP) {
                        clip.framePosition = 0
                        clip.start()
                    }
                }
            }
        }
    }

    actual suspend fun stop() {
        withContext(Dispatchers.IO) {
            clip.stop()
        }
    }

    actual suspend fun release() {
        withContext(Dispatchers.IO) {
            clip.stop()
            clip.close()
        }
    }
}