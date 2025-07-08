package com.hilingual.core.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicTextField(
    value: String,
    placeholder: String,
    textStyle: TextStyle,
    onValueChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    borderModifier: Modifier = Modifier,
    backgroundColor: Color = HilingualTheme.colors.gray100,
    focusRequester: FocusRequester = FocusRequester(),
    singleLine: Boolean = true,  // 한 줄 입력만 허용할지 여부
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardImeAction: ImeAction = ImeAction.Done,  // 키보드 오른쪽 아래 버튼 (기본값: Done)
    onDoneAction: () -> Unit = {},  // 키보드의 완료 액션이 눌렸을 때 실행되는 콜백
    onSearchAction: () -> Unit = {},  // 키보드의 검색 액션이 눌렸을 때 실행되는 콜백
    bottomRightContent: @Composable (() -> Unit)? = null
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .then(borderModifier)
            .padding(horizontal = 12.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(imeAction = keyboardImeAction),
        keyboardActions = KeyboardActions(
            onDone = { onDoneAction() },
            onSearch = { onSearchAction() }
        ),
        textStyle = textStyle.copy(
            color = HilingualTheme.colors.black
        ),
        decorationBox = { innerTextField ->
            Box {
                val columnModifier = if (bottomRightContent == null) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp, bottom = 39.dp)
                }

                val columnVerticalArrangement = if (bottomRightContent == null) {
                    Arrangement.Center
                } else {
                    Arrangement.SpaceBetween
                }

                val rowVerticalAlignment = if (bottomRightContent == null) {
                    Alignment.CenterVertically
                } else {
                    Alignment.Top
                }

                Column(
                    modifier = columnModifier,
                    verticalArrangement = columnVerticalArrangement
                ) {
                    // 아이콘-텍스트-아이콘 영역
                    Row(
                        verticalAlignment = rowVerticalAlignment
                    ) {
                        // 아이콘
                        leadingIcon?.let { it() }

                        // 텍스트
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            innerTextField()
                            // placeholder
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = HilingualTheme.colors.gray400,
                                    style = textStyle
                                )
                            }
                        }

                        // 아이콘
                        trailingIcon?.let { it() }
                    }
                }

                // 하단 영역 (존재하는 경우에만)
                bottomRightContent?.let {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        it()
                    }
                }
            }
        }
    )
}