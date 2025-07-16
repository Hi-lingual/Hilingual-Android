package com.hilingual.presentation.voca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.addFocusCleaner
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.event.LocalDialogController
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualBlack
import com.hilingual.data.voca.model.GroupingVocaModel
import com.hilingual.data.voca.model.VocaItemModel
import com.hilingual.presentation.voca.component.AddVocaButton
import com.hilingual.presentation.voca.component.VocaCard
import com.hilingual.presentation.voca.component.VocaEmptyCard
import com.hilingual.presentation.voca.component.VocaEmptyCardType
import com.hilingual.presentation.voca.component.VocaHeader
import com.hilingual.presentation.voca.component.VocaInfo
import com.hilingual.presentation.voca.component.VocaModal
import com.hilingual.presentation.voca.component.WordSortBottomSheet
import com.hilingual.presentation.voca.component.WordSortType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
internal fun VocaRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    viewModel: VocaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val localSystemBarsColor = LocalSystemBarsColor.current
    var isVocaModalVisibility by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val dialogController = LocalDialogController.current

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = hilingualBlack,
            isDarkIcon = false
        )
    }

    LaunchedEffect(uiState.vocaItemDetail) {
        isVocaModalVisibility = uiState.vocaItemDetail is UiState.Success
        if (isVocaModalVisibility) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(uiState.sortType) {
        viewModel.fetchWords(uiState.sortType)
    }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { event ->
            when (event) {
                is VocaSideEffect.ShowRetryDialog -> {
                    dialogController.show { dialogController.dismiss() }
                }
            }
        }
    }

    val isLoading = uiState.vocaGroupList is UiState.Loading

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        with(uiState) {
            VocaScreen(
                paddingValues = paddingValues,
                viewType = viewType,
                sortType = sortType,
                vocaCount = vocaCount,
                vocaGroupList = (vocaGroupList as? UiState.Success)?.data ?: persistentListOf(),
                searchResultList = searchResultList,
                searchText = searchKeyword,
                onSortTypeChanged = viewModel::updateSort,
                onCardClick = viewModel::fetchVocaDetail,
                onBookmarkClick = { phraseId, isMarked ->
                    viewModel.toggleBookmark(phraseId = phraseId, isMarked = isMarked)
                },
                onSearchTextChanged = viewModel::updateSearchKeyword,
                onWriteDiaryClick = navigateToHome,
                onCloseButtonClick = viewModel::clearSearchKeyword
            )
        }
    }

    when (val state = uiState.vocaItemDetail) {
        is UiState.Success -> {
            val vocaDetail = state.data
            if (isVocaModalVisibility) {
                Box(
                    modifier = Modifier
                        .background(HilingualTheme.colors.dim)
                        .noRippleClickable(onClick = {
                            isVocaModalVisibility = false
                            viewModel.clearSelectedVocaDetail()
                        })
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    VocaModal(
                        phraseId = vocaDetail.phraseId,
                        phrase = vocaDetail.phrase,
                        phraseType = vocaDetail.phraseType.toPersistentList(),
                        explanation = vocaDetail.explanation,
                        writtenDate = vocaDetail.writtenDate,
                        isBookmarked = vocaDetail.isBookmarked,
                        onBookmarkClick = { phraseId, isMarked ->
                            viewModel.toggleBookmark(phraseId = phraseId, isMarked = isMarked)
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .navigationBarsPadding()
                    )
                }
            }
        }

        else -> {}
    }
}

@Composable
private fun VocaScreen(
    paddingValues: PaddingValues,
    viewType: ScreenType,
    sortType: WordSortType,
    vocaCount: Int,
    vocaGroupList: ImmutableList<GroupingVocaModel>,
    searchResultList: ImmutableList<VocaItemModel>,
    searchText: String,
    onWriteDiaryClick: () -> Unit,
    onSortTypeChanged: (WordSortType) -> Unit,
    onCardClick: (Long) -> Unit,
    onBookmarkClick: (Long, Boolean) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onCloseButtonClick: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isFabVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 5
        }
    }

    LaunchedEffect(viewType) {
        if (viewType == ScreenType.DEFAULT) {
            listState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HilingualTheme.colors.gray100)
                .addFocusCleaner(focusManager)
        ) {
            VocaHeader(
                searchText = { searchText },
                onSearchTextChanged = onSearchTextChanged,
                onCloseButtonClick = onCloseButtonClick,
                modifier = Modifier
                    .background(hilingualBlack)
                    .fillMaxWidth()
            )

            when (viewType) {
                ScreenType.DEFAULT -> {
                    VocaListWithInfoSection(
                        listState = listState,
                        vocaGroupList = vocaGroupList,
                        sortType = sortType,
                        wordCount = vocaCount,
                        onSortClick = { showBottomSheet = true },
                        onCardClick = onCardClick,
                        onBookmarkClick = onBookmarkClick,
                        onWriteDiaryClick = onWriteDiaryClick
                    )
                }

                ScreenType.SEARCH -> {
                    SearchResultSection(
                        listState = listState,
                        searchResultList = searchResultList,
                        onCardClick = onCardClick,
                        onBookmarkClick = onBookmarkClick
                    )
                }
            }
        }
        HilingualFloatingButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            isVisible = isFabVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 16.dp)
        )
    }

    if (showBottomSheet) {
        WordSortBottomSheet(
            selectedType = sortType,
            onDismiss = { showBottomSheet = false },
            onTypeSelected = onSortTypeChanged
        )
    }
}

@Composable
private fun VocaListWithInfoSection(
    listState: LazyListState,
    vocaGroupList: ImmutableList<GroupingVocaModel>,
    sortType: WordSortType,
    wordCount: Int,
    onWriteDiaryClick: () -> Unit,
    onSortClick: () -> Unit,
    onCardClick: (Long) -> Unit,
    onBookmarkClick: (Long, Boolean) -> Unit
) {
    val isEmpty = vocaGroupList.all { it.words.isEmpty() }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (isEmpty) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize()
                        .padding(top = 120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VocaEmptyCard(type = VocaEmptyCardType.NOT_ADD)
                    Spacer(modifier = Modifier.height(16.dp))
                    AddVocaButton(onClick = onWriteDiaryClick)
                }
            }
        } else {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                VocaInfo(
                    wordCount = wordCount,
                    sortType = sortType,
                    onSortClick = onSortClick,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            vocaGroupList.forEach { group ->
                if (group.words.isNotEmpty()) {
                    item {
                        val groupLabel = when (sortType) {
                            WordSortType.Latest -> when (group.group) {
                                "today" -> "오늘"
                                "7days" -> "7일 전"
                                "30days" -> "30일 전"
                                else -> group.group
                            }

                            WordSortType.AtoZ -> group.group
                        }

                        Text(
                            text = groupLabel,
                            style = HilingualTheme.typography.bodySB16,
                            color = HilingualTheme.colors.black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    itemsIndexed(
                        group.words,
                        key = { _, voca -> voca.phraseId }
                    ) { index, voca ->
                        val isLastItem = index == group.words.lastIndex
                        VocaCard(
                            phrase = voca.phrase,
                            phraseType = voca.phraseType.toPersistentList(),
                            onCardClick = { onCardClick(voca.phraseId) },
                            isBookmarked = voca.isBookmarked,
                            onBookmarkClick = { onBookmarkClick(voca.phraseId, !voca.isBookmarked) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (isLastItem) 20.dp else 16.dp)
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SearchResultSection(
    listState: LazyListState,
    searchResultList: ImmutableList<VocaItemModel>,
    onCardClick: (Long) -> Unit,
    onBookmarkClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (searchResultList.isEmpty()) {
        VocaEmptyCard(
            type = VocaEmptyCardType.NOT_SEARCH,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 80.dp, vertical = 80.dp)
        )
    } else {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(
                searchResultList,
                key = { voca -> voca.phraseId }
            ) { voca ->
                VocaCard(
                    phrase = voca.phrase,
                    phraseType = voca.phraseType.toPersistentList(),
                    onCardClick = { onCardClick(voca.phraseId) },
                    isBookmarked = voca.isBookmarked,
                    onBookmarkClick = { onBookmarkClick(voca.phraseId, !voca.isBookmarked) },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
