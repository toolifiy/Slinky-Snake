package com.example.slinkysnake.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.PI
import kotlin.math.sin

object SoundSynth {

    private const val SAMPLE_RATE = 22050
    var isSoundEnabled: Boolean = true
    var soundVolume: Float = 0.8f

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<String, Int>()
    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        val appContext = context.applicationContext
        Thread {
            try {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

                val pool = SoundPool.Builder()
                    .setMaxStreams(12)
                    .setAudioAttributes(audioAttributes)
                    .build()

                soundPool = pool

                val cacheDir = appContext.cacheDir

                loadSound(pool, cacheDir, "click", generateClickPcm())
                loadSound(pool, cacheDir, "coin", generateCoinPcm())
                loadSound(pool, cacheDir, "sell", generateSellPcm())
                loadSound(pool, cacheDir, "purchase", generatePurchasePcm())
                loadSound(pool, cacheDir, "eat_normal", generateEatPcm(350.0))
                loadSound(pool, cacheDir, "eat_combo1", generateEatPcm(450.0))
                loadSound(pool, cacheDir, "eat_combo2", generateEatPcm(550.0))
                loadSound(pool, cacheDir, "eat_combo3", generateEatPcm(650.0))
                loadSound(pool, cacheDir, "eat_booster", generateBoosterPcm())
                loadSound(pool, cacheDir, "eat_star", generateStarPcm())
                loadSound(pool, cacheDir, "eat_chili", generateChiliPcm())
                loadSound(pool, cacheDir, "crash", generateCrashPcm())
                loadSound(pool, cacheDir, "levelup", generateLevelUpPcm())
                loadSound(pool, cacheDir, "beep_low", generateBeepPcm(440.0, 80))
                loadSound(pool, cacheDir, "beep_high", generateBeepPcm(880.0, 160))

                isInitialized = true
            } catch (_: Throwable) {}
        }.start()
    }

    private fun loadSound(pool: SoundPool, cacheDir: File, key: String, pcm: ShortArray) {
        try {
            val wavFile = File(cacheDir, "slinky_$key.wav")
            writeWavFile(wavFile, pcm, SAMPLE_RATE)
            val soundId = pool.load(wavFile.absolutePath, 1)
            soundMap[key] = soundId
        } catch (_: Throwable) {}
    }

    private fun play(key: String, rate: Float = 1.0f) {
        if (!isSoundEnabled || soundVolume <= 0f) return
        val pool = soundPool ?: return
        val soundId = soundMap[key] ?: return
        try {
            val vol = soundVolume.coerceIn(0f, 1f)
            pool.play(soundId, vol, vol, 1, 0, rate)
        } catch (_: Throwable) {}
    }

    fun playClick() {
        // Disabled as requested: completely silent on clicks/navigation
    }

    fun playCoin() {
        play("coin")
    }

    fun playSell() {
        play("sell")
    }

    fun playPurchase() {
        play("purchase")
    }

    fun playEat(type: String = "APPLE", combo: Int = 0) {
        when (type) {
            "BOOSTER" -> play("eat_booster")
            "GOLDEN_STAR" -> play("eat_star")
            "CHILI" -> play("eat_chili")
            else -> {
                // Keep the satisfying 1st normal water/pop sound exclusively, no sharp combo sounds
                play("eat_normal")
            }
        }
    }

    fun playCrash() {
        play("crash")
    }

    fun playLevelUp() {
        play("levelup")
    }

    fun playCountdownBeep(isGo: Boolean) {
        if (isGo) play("beep_high") else play("beep_low")
    }

    fun playAchievement() {
        // Disabled per user preference
    }

    // --- PCM Generators ---

    private fun generateClickPcm(): ShortArray {
        val durationMs = 45
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = i.toDouble() / numSamples
            val freq = 650.0 + progress * 700.0
            val envelope = (1.0 - progress) * (1.0 - progress)
            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.45
            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateCoinPcm(): ShortArray {
        val durationMs = 120
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        val half = numSamples / 2
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val freq = if (i < half) 987.77 else 1318.51
            val progress = i.toDouble() / numSamples
            val envelope = (1.0 - progress) * (1.0 - progress)
            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.50
            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateSellPcm(): ShortArray {
        // High-energy arcade cash register & coin chime
        val notes = listOf(587.33, 880.0, 1174.66, 1760.0) // D5, A5, D6, A6
        val noteDurationMs = 45
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
                val envelope = (1.0 - progress) * 0.95
                val sample = (sin(2.0 * PI * freq * t) + 0.3 * sin(4.0 * PI * freq * t)) * envelope * Short.MAX_VALUE * 0.45
                buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
        }
        return buffer
    }

    private fun generatePurchasePcm(): ShortArray {
        val notes = listOf(523.25, 659.25, 783.99, 1046.50)
        val noteDurationMs = 60
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
                val envelope = 1.0 - progress * 0.5
                val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.45
                buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
        }
        return buffer
    }

    private fun generateEatPcm(baseFreq: Double): ShortArray {
        val durationMs = 85
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = i.toDouble() / numSamples
            val freq = baseFreq + progress * (baseFreq * 1.5)
            val envelope = (1.0 - progress) * (1.0 - progress)
            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.55
            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateBoosterPcm(): ShortArray {
        val durationMs = 220
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = i.toDouble() / numSamples
            val freq = 280.0 + progress * progress * 1400.0
            val envelope = (1.0 - progress) * (1.0 - progress)
            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.55
            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateStarPcm(): ShortArray {
        val durationMs = 200
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = i.toDouble() / numSamples
            val freq = 520.0 + progress * 1300.0
            val envelope = 1.0 - progress
            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.50
            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateChiliPcm(): ShortArray {
        val durationMs = 180
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = i.toDouble() / numSamples
            val freq = 300.0 + progress * 950.0
            val sample = ((t * freq % 1.0) * 2.0 - 1.0) * (1.0 - progress) * Short.MAX_VALUE * 0.40
            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateCrashPcm(): ShortArray {
        val durationMs = 280
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = i.toDouble() / numSamples
            val freq = 280.0 * (1.0 - progress * 0.8)
            val noise = (Math.random() * 2.0 - 1.0) * 0.4
            val tone = sin(2.0 * PI * freq * t) * 0.6
            val sample = (tone + noise) * (1.0 - progress) * Short.MAX_VALUE * 0.55
            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateLevelUpPcm(): ShortArray {
        val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)
        val noteDurationMs = 80
        val totalDurationMs = noteDurationMs * notes.size + 100
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
                val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.50
                buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
        }
        return buffer
    }

    private fun generateBeepPcm(freq: Double, durationMs: Int): ShortArray {
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = i.toDouble() / numSamples
            val envelope = 1.0 - progress
            val sample = sin(2.0 * PI * freq * t) * envelope * Short.MAX_VALUE * 0.50
            buffer[i] = sample.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun writeWavFile(file: File, pcm: ShortArray, sampleRate: Int) {
        val byteData = ByteBuffer.allocate(pcm.size * 2).order(ByteOrder.LITTLE_ENDIAN)
        for (sample in pcm) {
            byteData.putShort(sample)
        }
        val pcmBytes = byteData.array()
        val totalDataLen = pcmBytes.size + 36
        val byteRate = sampleRate * 2 // 16-bit mono

        val header = ByteArray(44)
        // RIFF/WAVE header
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // PCM
        header[21] = 0
        header[22] = 1 // Mono (1 channel)
        header[23] = 0
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = ((sampleRate shr 8) and 0xff).toByte()
        header[26] = ((sampleRate shr 16) and 0xff).toByte()
        header[27] = ((sampleRate shr 24) and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()
        header[32] = 2 // block align
        header[33] = 0
        header[34] = 16 // bits per sample
        header[35] = 0
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (pcmBytes.size and 0xff).toByte()
        header[41] = ((pcmBytes.size shr 8) and 0xff).toByte()
        header[42] = ((pcmBytes.size shr 16) and 0xff).toByte()
        header[43] = ((pcmBytes.size shr 24) and 0xff).toByte()

        FileOutputStream(file).use { out ->
            out.write(header)
            out.write(pcmBytes)
        }
    }
}
