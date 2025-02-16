package ru.markn.engine.audio

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

actual class AudioPlayer actual constructor(
    bytes: ByteArray
) : KoinComponent {
    private val context: Context by inject()
    private var mediaPlayer: MediaPlayer = run {
        val tempFile = File.createTempFile("temp_audio", ".mp3", context.cacheDir)
        tempFile.writeBytes(bytes)
        MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            prepareAsync()
        }
    }

    actual suspend fun play(url: String) {
        withContext(Dispatchers.IO) {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { start() }
                setOnCompletionListener { release() }
            }
        }
    }

    actual suspend fun play(isRepeated: Boolean) {
        withContext(Dispatchers.IO) {
            mediaPlayer.stop()
            mediaPlayer.apply {
                setOnPreparedListener { start() }
                setOnCompletionListener {
                    if (isRepeated) {
                        start()
                    }
                }
            }
        }
    }

    actual suspend fun stop() {
        withContext(Dispatchers.IO) {
            mediaPlayer.stop()
        }
    }

    actual suspend fun release() {
        withContext(Dispatchers.IO) {
            mediaPlayer.reset()
            mediaPlayer.release()
        }
    }
}