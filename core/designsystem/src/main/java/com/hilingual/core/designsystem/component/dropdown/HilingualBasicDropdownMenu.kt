package com.hilingual.core.designsystem.component.dropdown

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.hilingual.core.common.extension.dropShadow
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicDropdownMenu(
    modifier: Modifier = Modifier,
    offsetY: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var iconHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val popupYOffset = with(density) { offsetY.roundToPx() }

    Box {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    iconHeight = coordinates.size.height
                }
                .clickable(onClick = { expanded = true })
        )

        if (expanded) {
            Popup(
                alignment = Alignment.TopEnd,
                onDismissRequest = { expanded = false },
                offset = IntOffset(x = 0, y = iconHeight + popupYOffset),
            ) {
                Column(
                    modifier = modifier
                        .dropShadow(
                            color = HilingualTheme.colors.black.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(size = 10.dp),
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            spread = 0.dp,
                            blur = 15.dp
                        )
                        .clip(RoundedCornerShape(size = 10.dp))
                        .background(color = Color.White)
                        .width(182.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun HilingualDropdownMenuItem(
    text: String,
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconResId),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Text(
            text = text,
            style = HilingualTheme.typography.bodySB14,
            color = HilingualTheme.colors.gray700
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DropdownMenuPreview() {
    HilingualTheme {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HilingualBasicDropdownMenu {
                HilingualDropdownMenuItem(
                    text = "Option 1",
                    iconResId = R.drawable.ic_search_20,
                    onClick = {
                        Log.d("DropdownMenu", "Option 1 clicked")
                    }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = HilingualTheme.colors.gray200
                )
                HilingualDropdownMenuItem(
                    text = "Option 2",
                    iconResId = R.drawable.ic_search_20,
                    onClick = {
                        Log.d("DropdownMenu", "Option 2 clicked")
                    }
                )
            }
        }
    }
}