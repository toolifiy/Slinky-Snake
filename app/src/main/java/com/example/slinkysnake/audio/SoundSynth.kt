package com.example.slinkysnake.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

object SoundSynth {

    private const val SAMPLE_RATE = 22050
    private val audioScope = CoroutineScope(Dispatchers.Default)

    var isSoundEnabled: Boolean = true
    var soundVolume: Float = 0.8f

    fun playClick() {
        if (!isSoundEnabled || soundVolume <= 0f) return
        audioScope.launch {
            try {
                val durationMs = 50
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val progress = i.toDouble() / numSamples
                    val freq = 600.0 + progress * 600.0
                    val envelope = 1.0 - progress
                    val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.4 * soundVolume
                    buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                playPcm(buffer)
            } catch (_: Throwable) {}
        }
    }

    fun playCoin() {
        if (!isSoundEnabled || soundVolume <= 0f) return
        audioScope.launch {
            try {
                val durationMs = 120
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)
                val half = numSamples / 2
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val freq = if (i < half) 987.77 else 1318.51 // B5 to E6 chime
                    val progress = i.toDouble() / numSamples
                    val envelope = (1.0 - progress) * (1.0 - progress)
                    val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.45 * soundVolume
                    buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                playPcm(buffer)
            } catch (_: Throwable) {}
        }
    }

    fun playPurchase() {
        if (!isSoundEnabled || soundVolume <= 0f) return
        audioScope.launch {
            try {
                val notes = listOf(523.25, 659.25, 783.99, 1046.50) // C5 - E5 - G5 - C6 fanfare
                val noteDurationMs = 70
                val totalDurationMs = notes.size * noteDurationMs
                val numSamples = (SAMPLE_RATE * (totalDurationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                for (noteIdx in notes.indices) {
                    val startSample = (noteIdx * noteDurationMs * SAMPLE_RATE / 1000)
                    val endSample = if (noteIdx == notes.size - 1) numSamples else ((noteIdx + 1) * noteDurationMs * SAMPLE_RATE / 1000)
                    val freq = notes[noteIdx]

                    for (i in startSample until endSample.coerceAtMost(numSamples)) {
                        val t = (i - startSample).toDouble() / SAMPLE_RATE
                        val progress = (i - startSample).toDouble() / (endSample - startSample)
                        val envelope = 1.0 - progress * 0.4
                        val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.45 * soundVolume
                        buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    }
                }
                playPcm(buffer)
            } catch (_: Throwable) {}
        }
    }

    fun playEat(type: String = "APPLE", combo: Int = 0) {
        if (!isSoundEnabled || soundVolume <= 0f) return
        audioScope.launch {
            try {
                when (type) {
                    "BOOSTER" -> {
                        val durationMs = 300
                        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                        val buffer = ShortArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / SAMPLE_RATE
                            val progress = i.toDouble() / numSamples
                            val freq = 260.0 + progress * progress * 1300.0
                            val envelope = (1.0 - progress) * (1.0 - progress)
                            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.5 * soundVolume
                            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                        }
                        playPcm(buffer)
                    }
                    "GOLDEN_STAR" -> {
                        val durationMs = 250
                        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                        val buffer = ShortArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / SAMPLE_RATE
                            val progress = i.toDouble() / numSamples
                            val freq = 440.0 + progress * 1200.0
                            val envelope = 1.0 - progress
                            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.45 * soundVolume
                            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                        }
                        playPcm(buffer)
                    }
                    "CHILI" -> {
                        val durationMs = 200
                        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                        val buffer = ShortArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / SAMPLE_RATE
                            val progress = i.toDouble() / numSamples
                            val freq = 250.0 + progress * 900.0
                            val sample = ((t * freq % 1.0) * 2.0 - 1.0) * (1.0 - progress) * Short.MAX_VALUE * 0.35 * soundVolume
                            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                        }
                        playPcm(buffer)
                    }
                    else -> {
                        val durationMs = 100
                        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                        val buffer = ShortArray(numSamples)
                        val baseFreq = 350.0 + (combo.coerceAtMost(10) * 50.0)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / SAMPLE_RATE
                            val progress = i.toDouble() / numSamples
                            val freq = baseFreq + progress * (baseFreq * 1.8)
                            val envelope = (1.0 - progress) * (1.0 - progress)
                            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.5 * soundVolume
                            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                        }
                        playPcm(buffer)
                    }
                }
            } catch (_: Throwable) {}
        }
    }

    fun playCrash() {
        if (!isSoundEnabled || soundVolume <= 0f) return
        audioScope.launch {
            try {
                val durationMs = 400
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val progress = i.toDouble() / numSamples
                    val freq = 320.0 * (1.0 - progress * 0.8)
                    val noise = (Math.random() * 2.0 - 1.0) * 0.35
                    val tone = sin(2.0 * PI * freq * t) * 0.65
                    val sample = (tone + noise) * (1.0 - progress) * Short.MAX_VALUE * 0.5 * soundVolume
                    buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                playPcm(buffer)
            } catch (_: Throwable) {}
        }
    }

    fun playLevelUp() {
        if (!isSoundEnabled || soundVolume <= 0f) return
        audioScope.launch {
            try {
                val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)
                val noteDurationMs = 90
                val totalDurationMs = noteDurationMs * notes.size + 150
                val numSamples = (SAMPLE_RATE * (totalDurationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                for (noteIdx in notes.indices) {
                    val startSample = (noteIdx * noteDurationMs * SAMPLE_RATE / 1000)
                    val endSample = if (noteIdx == notes.size - 1) numSamples else ((noteIdx + 1) * noteDurationMs * SAMPLE_RATE / 1000)
                    val freq = notes[noteIdx]

                    for (i in startSample until endSample) {
                        val t = (i - startSample).toDouble() / SAMPLE_RATE
                        val progress = (i - startSample).toDouble() / (endSample - startSample)
                        val envelope = (1.0 - progress) * (1.0 - progress)
                        val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.45 * soundVolume
                        buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    }
                }
                playPcm(buffer)
            } catch (_: Throwable) {}
        }
    }

    fun playCountdownBeep(isGo: Boolean) {
        if (!isSoundEnabled || soundVolume <= 0f) return
        audioScope.launch {
            try {
                val durationMs = if (isGo) 180 else 90
                val freq = if (isGo) 880.0 else 440.0
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val progress = i.toDouble() / numSamples
                    val envelope = 1.0 - progress
                    val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.45 * soundVolume
                    buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                playPcm(buffer)
            } catch (_: Throwable) {}
        }
    }

    fun playAchievement() {
        // Disabled per user preference
    }

    private fun playPcm(buffer: ShortArray) {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(1024)

            val bufferSizeBytes = (buffer.size * 2).coerceAtLeast(minBufferSize)
            val paddedBuffer = if (buffer.size * 2 < bufferSizeBytes) {
                val targetSize = bufferSizeBytes / 2
                val padded = ShortArray(targetSize)
                System.arraycopy(buffer, 0, padded, 0, buffer.size)
                padded
            } else {
                buffer
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSizeBytes)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(paddedBuffer, 0, paddedBuffer.size)
            audioTrack.play()

            // Safe release after playback duration
            audioScope.launch {
                try {
                    val durationMs = ((paddedBuffer.size.toDouble() / SAMPLE_RATE) * 1000).toLong() + 100L
                    kotlinx.coroutines.delay(durationMs)
                    if (audioTrack.state == AudioTrack.STATE_INITIALIZED) {
                        audioTrack.stop()
                        audioTrack.release()
                    }
                } catch (_: Throwable) {}
            }
        } catch (_: Throwable) {}
    }
}

