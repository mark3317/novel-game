package ru.markn.engine.audio

import javazoom.jl.decoder.Bitstream
import javazoom.jl.decoder.Decoder
import javazoom.jl.decoder.Header
import javazoom.jl.decoder.SampleBuffer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.ByteArrayInputStream
import java.io.File
import javax.sound.sampled.*
import kotlin.math.log10
import kotlin.math.round
import kotlin.time.Duration

actual class AudioPlayer {
    private var trackJob: Job? = null
    private val playerJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO + playerJob)
    private val _volumeFlow = MutableStateFlow(0.5f)
    private val _stateFlow = MutableStateFlow(PlayerState.IDLE)
    private val sourceDataLine = AudioSystem.getSourceDataLine(
        AudioFormat(44100f, 16, 2, true, false)
    ).apply { open() }

    actual val stateFlow: StateFlow<PlayerState>
        get() = _stateFlow.asStateFlow()

    actual val volumeFlow: StateFlow<Float>
        get() = _volumeFlow.asStateFlow()

    init {
        _stateFlow.onEach {
            when (it) {
                PlayerState.PLAYING -> sourceDataLine.start()
                PlayerState.PAUSED -> sourceDataLine.stop()
                PlayerState.IDLE -> {
                    trackJob?.cancel()
                    sourceDataLine.stop()
                    sourceDataLine.flush()
                }
                PlayerState.CLOSED -> {
                    trackJob?.cancel()
                    playerJob.cancel()
                    sourceDataLine.close()
                }
            }
        }.launchIn(scope)
        _volumeFlow.onEach {
            sourceDataLine.setVolume(it)
        }.launchIn(scope)
    }

    actual fun play(url: String) {
        trackJob?.cancel()
        sourceDataLine.flush()
        _stateFlow.value = PlayerState.PLAYING
        play(Bitstream(AudioSystem.getAudioInputStream(File(url))))
    }

    actual fun play(bytes: ByteArray) {
        trackJob?.cancel()
        sourceDataLine.flush()
        _stateFlow.value = PlayerState.PLAYING
        play(Bitstream(ByteArrayInputStream(bytes)))
    }

    actual fun setVolume(volume: Float) {
        _volumeFlow.value = volume
    }

    actual fun pause() {
        if (trackJob?.isActive == true) {
            _stateFlow.value = PlayerState.PAUSED
        }
    }

    actual fun resume() {
        if (trackJob?.isActive == true) {
            _stateFlow.value = PlayerState.PLAYING
        }
    }

    actual fun stop(durationFadeOut: Duration) {
        scope.launch {
            if (trackJob?.isActive == true && durationFadeOut > Duration.ZERO) {
                val currentVolume = _volumeFlow.value
                for (i in 99 downTo 0) {
                    val volumeFadeOut = round(currentVolume * (i / 100f) * 100) / 100
                    volumeFadeOut.takeIf { it != _volumeFlow.value }?.let {
                        setVolume(volumeFadeOut)
                    }
                    delay(durationFadeOut / 100)
                }
                sourceDataLine.stop()
                setVolume(currentVolume)
            }
            _stateFlow.value = PlayerState.IDLE
        }
    }

    actual fun close() {
        _stateFlow.value = PlayerState.CLOSED
    }

    private fun play(bitstream: Bitstream) {
        trackJob = scope.launch {
            val decoder = Decoder()
            var header: Header
            while (bitstream.readFrame().also { header = it } != null) {
                _stateFlow.first { it == PlayerState.PLAYING }
                val sampleBuffer = (decoder.decodeFrame(header, bitstream) as SampleBuffer).buffer.toByteArray()
                sourceDataLine.write(sampleBuffer, 0, sampleBuffer.size)
                bitstream.closeFrame()
            }
            _stateFlow.value = PlayerState.IDLE
        }
    }

    private fun SourceDataLine.setVolume(volume: Float) {
        val gain = log10(volume.coerceIn(0f, 1f)) * 20.0f
        val volControl = getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
        volControl.value = gain
    }

    private fun ShortArray.toByteArray(): ByteArray = ByteArray(size * 2).also {
        for (i in indices) {
            it[i * 2] = this[i].toByte()
            it[i * 2 + 1] = (this[i].toInt() ushr 8).toByte()
        }
    }
}