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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale

private const val VOCA_TTS_UTTERANCE_ID = "voca_tts"

@Stable
internal class VocaTtsState(scope: CoroutineScope) {
    private val ttsRef = arrayOfNulls<TextToSpeech>(1)
    private val doneChannel = Channel<Unit>(Channel.UNLIMITED)

    var isPlaying by mutableStateOf(false)
        private set

    init {
        scope.launch {
            doneChannel.receiveAsFlow().collect { isPlaying = false }
        }
    }

    internal fun onTtsReady(tts: TextToSpeech) {
        tts.setOnUtteranceProgressListener(
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
        ttsRef[0] = tts
    }

    internal fun release() {
        doneChannel.close()
        ttsRef[0]?.setOnUtteranceProgressListener(null)
        ttsRef[0]?.stop()
        ttsRef[0]?.shutdown()
        ttsRef[0] = null
    }

    fun toggle(text: String) {
        val tts = ttsRef[0] ?: return
        if (isPlaying) {
            tts.stop()
            isPlaying = false
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, VOCA_TTS_UTTERANCE_ID)
            isPlaying = true
        }
    }

    fun stop() {
        ttsRef[0]?.stop()
        isPlaying = false
    }
}

@Composable
internal fun rememberVocaTts(): VocaTtsState {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = remember { VocaTtsState(scope) }

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
