package com.hilingual.core.ui.component.item.diary.card.diarycard

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

private const val TTS_UTTERANCE_ID = "diary_tts"

private sealed interface TtsEvent {
    data object Done : TtsEvent
    data class RangeStart(val start: Int, val end: Int) : TtsEvent
}

@Stable
internal class TtsController(scope: CoroutineScope) {
    private var tts: TextToSpeech? = null
    private val _events = Channel<TtsEvent>(Channel.UNLIMITED)
    private var resumeOffset = 0
    private var lastRangeStart = 0

    var isPlaying by mutableStateOf(false)
        private set
    var spokenUpTo by mutableIntStateOf(0)
        private set

    init {
        scope.launch {
            _events.receiveAsFlow().collect { event ->
                when (event) {
                    TtsEvent.Done -> onDone()
                    is TtsEvent.RangeStart -> onRange(event.start, event.end)
                }
            }
        }
    }

    internal fun onTtsReady(instance: TextToSpeech) {
        tts = instance
        instance.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) = Unit

                override fun onDone(utteranceId: String) {
                    _events.trySend(TtsEvent.Done)
                }

                @Suppress("OVERRIDE_DEPRECATION")
                override fun onError(utteranceId: String) = Unit

                override fun onError(utteranceId: String, errorCode: Int) {
                    _events.trySend(TtsEvent.Done)
                }

                override fun onRangeStart(utteranceId: String, start: Int, end: Int, frame: Int) {
                    _events.trySend(TtsEvent.RangeStart(start, end))
                }
            },
        )
    }

    fun toggle(text: String) {
        if (isPlaying) pause() else play(text)
    }

    fun playFrom(sentenceStart: Int, text: String) {
        tts?.stop()
        spokenUpTo = sentenceStart
        resumeOffset = sentenceStart
        lastRangeStart = sentenceStart
        tts?.let { instance ->
            instance.speak(text.substring(sentenceStart), TextToSpeech.QUEUE_FLUSH, null, TTS_UTTERANCE_ID)
            isPlaying = true
        }
    }

    fun stop() {
        tts?.stop()
        isPlaying = false
        spokenUpTo = 0
        resumeOffset = 0
        lastRangeStart = 0
    }

    internal fun release() {
        tts?.setOnUtteranceProgressListener(null)
        _events.close()
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    private fun pause() {
        tts?.stop()
        isPlaying = false
        // 재개 시 현재 단어부터 다시 읽도록 spokenUpTo를 단어 시작 위치로 되돌림
        spokenUpTo = lastRangeStart
    }

    private fun onDone() {
        isPlaying = false
        spokenUpTo = 0
        resumeOffset = 0
        lastRangeStart = 0
    }

    private fun onRange(start: Int, end: Int) {
        lastRangeStart = resumeOffset + start
        spokenUpTo = resumeOffset + end
    }

    private fun play(text: String) {
        if (spokenUpTo >= text.length) spokenUpTo = 0
        val textToSpeak = if (spokenUpTo > 0) text.substring(spokenUpTo) else text
        resumeOffset = spokenUpTo
        lastRangeStart = spokenUpTo
        tts?.let { instance ->
            instance.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, TTS_UTTERANCE_ID)
            isPlaying = true
        }
    }
}

@Composable
internal fun rememberTtsController(): TtsController {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val controller = remember { TtsController(scope) }

    DisposableEffect(Unit) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
                tts?.let { controller.onTtsReady(it) }
            }
        }
        onDispose { controller.release() }
    }

    return controller
}
