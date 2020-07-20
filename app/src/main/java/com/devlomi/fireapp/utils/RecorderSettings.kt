package com.devlomi.fireapp.utils

import android.media.AudioFormat
import android.media.MediaRecorder
import omrecorder.AudioRecordConfig
import omrecorder.PullableSource

object RecorderSettings {
    //if you want to improve audio quality change it to 44100
    //but keep in mind this will increase the file size
    private const val freq = 12000

    @JvmStatic
    fun getMic(): PullableSource {
        return PullableSource.AutomaticGainControl(
                PullableSource.Default(
                        AudioRecordConfig.Default(
                                MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                                AudioFormat.CHANNEL_IN_MONO, freq
                        )
                )
        )
    }
}