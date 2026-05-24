/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.voca.component

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val VOCA_TTS_UTTERANCE_ID = "voca_tts"

@Stable
internal class VocaTtsController(scope: CoroutineScope) {
    private var tts: TextToSpeech? = null
    private val doneChannel = Channel<Unit>(Channel.UNLIMITED)

    var isPlaying by mutableStateOf(false)
        private set

    init {
        scope.launch {
            doneChannel.receiveAsFlow().collect { isPlaying = false }
        }
    }

    internal fun onTtsReady(instance: TextToSpeech) {
        instance.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) = Unit
                override fun onDone(utteranceId: String) {
                    doneChannel.trySend(Unit)
                }

                @Suppress("OVERRIDE_DEPRECATION")
                override fun onError(utteranceId: String) = Unit
                override fun onError(utteranceId: String, errorCode: Int) {
                    doneChannel.trySend(Unit)
                }
            },
        )
        tts = instance
    }

    internal fun release() {
        doneChannel.close()
        tts?.setOnUtteranceProgressListener(null)
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    fun toggle(text: String) {
        val instance = tts ?: return
        if (isPlaying) {
            instance.stop()
            isPlaying = false
        } else {
            instance.speak(text, TextToSpeech.QUEUE_FLUSH, null, VOCA_TTS_UTTERANCE_ID)
            isPlaying = true
        }
    }

    fun stop() {
        tts?.stop()
        isPlaying = false
    }
}

@Composable
internal fun rememberVocaTts(): VocaTtsController {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = remember { VocaTtsController(scope) }

    DisposableEffect(Unit) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
                tts?.let { state.onTtsReady(it) }
            }
        }
        onDispose { state.release() }
    }

    return state
}
