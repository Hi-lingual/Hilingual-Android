package com.hilingual.core.ui.component.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.designsystem.component.snackbar.HilingualActionSnackbar
import com.hilingual.core.designsystem.component.toast.TextToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberMessageController(hostState: SnackbarHostState): (HilingualMessage) -> Unit {
    val coroutineScope = rememberCoroutineScope()
    return remember(coroutineScope, hostState) {
        { message ->
            coroutineScope.launch {
                hostState.currentSnackbarData?.dismiss()
                val job = launch {
                    hostState.showSnackbar(visuals = message)
                }
                delay(message.messageDuration.millis)
                job.cancel()
            }
        }
    }
}

@Composable
fun HilingualMessageHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(bottom = 106.dp),
    ) {
        SnackbarHost(hostState = hostState) { data ->
            when (val visuals = data.visuals) {
                is HilingualMessage.Snackbar -> {
                    HilingualActionSnackbar(
                        message = visuals,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onDismiss = { data.dismiss() },
                    )
                }

                is HilingualMessage.Toast -> {
                    TextToast(text = visuals.message)
                }

                else -> {
                    TextToast(text = visuals.message)
                }
            }
        }
    }
}
