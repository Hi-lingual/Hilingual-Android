package com.hilingual.core.designsystem.component.dropdown

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier.padding(16.dp)
    ) {
        IconButton(
            onClick = { onExpandedChange(!expanded) }
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(x = 0.dp, y = 4.dp),
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .background(HilingualTheme.colors.white)
                .width(182.dp)
                .padding(0.dp)
        ) {
            content()
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
    DropdownMenuItem(
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = HilingualTheme.colors.gray400
                )

                Text(
                    text = text,
                    style = HilingualTheme.typography.bodySB14,
                    color = HilingualTheme.colors.gray700
                )
            }
        },
        onClick = onClick,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    HilingualTheme {
        var expanded by remember { mutableStateOf(false) }

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HilingualBasicDropdownMenu(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                HilingualDropdownMenuItem(
                    text = "Option 1",
                    iconResId = R.drawable.ic_search_20,
                    onClick = {
                        expanded = false
                        Log.d("DropdownMenu", "Option 1 clicked")
                    }
                )
                HilingualDropdownMenuItem(
                    text = "Option 2",
                    iconResId = R.drawable.ic_search_20,
                    onClick = {
                        expanded = false
                        Log.d("DropdownMenu", "Option 2 clicked")
                    }
                )
            }
        }
    }
}