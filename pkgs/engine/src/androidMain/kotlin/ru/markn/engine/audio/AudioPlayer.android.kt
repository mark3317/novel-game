package ru.markn.engine.audio

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.time.Duration

actual class AudioPlayer {
    private val playerJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO + playerJob)
    private val _volumeFlow = MutableStateFlow(0.5f)
    private val _stateFlow = MutableStateFlow(PlayerState.IDLE)
    private var mediaPlayer = MediaPlayer().apply {
        setOnCompletionListener {
            _stateFlow.value = PlayerState.IDLE
        }
    }

    actual val stateFlow: StateFlow<PlayerState>
        get() = _stateFlow.asStateFlow()

    actual val volumeFlow: StateFlow<Float>
        get() = _volumeFlow.asStateFlow()

    init {
        _stateFlow.onEach {
            when (it) {
                PlayerState.PLAYING -> mediaPlayer.start()
                PlayerState.PAUSED -> mediaPlayer.pause()
                PlayerState.IDLE -> {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                }
                PlayerState.CLOSED -> {
                    playerJob.cancel()
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }
            }
        }.launchIn(scope)
        _volumeFlow.onEach {
            mediaPlayer.setVolume(it, it)
        }.launchIn(scope)
        GlobalAudioController.isAudioEnabledFlow.onEach {
            when (it) {
                true -> if (_stateFlow.value == PlayerState.PAUSED) _stateFlow.value = PlayerState.PLAYING
                false -> if (_stateFlow.value == PlayerState.PLAYING) _stateFlow.value = PlayerState.PAUSED
            }
        }.launchIn(scope)
    }

    actual fun play(bytes: ByteArray) {
        scope.launch {
            if (_stateFlow.value == PlayerState.PLAYING) {
                mediaPlayer.stop()
                mediaPlayer.reset()
            }
            mediaPlayer.setDataSource(MediaDataSource(bytes))
            mediaPlayer.prepare()
            when (_stateFlow.value) {
                PlayerState.PLAYING -> mediaPlayer.start()
                else -> _stateFlow.value = PlayerState.PLAYING
            }
        }
    }

    actual fun play(url: String) {
        scope.launch {
            if (_stateFlow.value == PlayerState.PLAYING) {
                mediaPlayer.stop()
                mediaPlayer.reset()
            }
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            when (_stateFlow.value) {
                PlayerState.PLAYING -> mediaPlayer.start()
                else -> _stateFlow.value = PlayerState.PLAYING
            }
        }
    }

    actual fun setVolume(volume: Float) {
        _volumeFlow.value = volume
    }

    actual fun pause() {
        _stateFlow.value = PlayerState.PAUSED
    }

    actual fun resume() {
        _stateFlow.value = PlayerState.PLAYING
    }

    actual fun stop(durationFadeOut: Duration) {
        scope.launch {
            if (mediaPlayer.isPlaying && durationFadeOut > Duration.ZERO) {
                val currentVolume = _volumeFlow.value
                for (i in 99 downTo 0) {
                    val volumeFadeOut = round(currentVolume * (i / 100f) * 100) / 100
                    volumeFadeOut.takeIf { it != _volumeFlow.value }?.let {
                        setVolume(volumeFadeOut)
                    }
                    delay(durationFadeOut / 100)
                }
                mediaPlayer.stop()
                setVolume(currentVolume)
            }
            _stateFlow.value = PlayerState.IDLE
        }
    }

    actual fun close() {
        _stateFlow.value = PlayerState.CLOSED
    }
}