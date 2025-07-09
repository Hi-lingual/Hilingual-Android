package com.hilingual.presentation.diaryfeedback.component

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun AIFeedbackSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = HilingualTheme.colors.white,
            checkedTrackColor = HilingualTheme.colors.hilingualOrange,
            uncheckedThumbColor = HilingualTheme.colors.white,
            uncheckedTrackColor = HilingualTheme.colors.gray300,
            uncheckedBorderColor = Color.Transparent,
        ),
        thumbContent = {
            Box(
                modifier = Modifier.size(18.dp)
            )
        },
        interactionSource = NoRippleInteractionSource(),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun SwitchPreview() {
    HilingualTheme {
        var checked by remember { mutableStateOf(true) }

        AIFeedbackSwitch(
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}

private class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions = emptyFlow<Interaction>()

    override suspend fun emit(interaction: Interaction) { }

    override fun tryEmit(interaction: Interaction): Boolean = true
}
