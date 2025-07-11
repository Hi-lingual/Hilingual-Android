package com.hilingual.presentation.voca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualBlack
import com.hilingual.core.designsystem.theme.hilingualOrange
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.voca.component.AddVocaButton
import com.hilingual.presentation.voca.component.VocaCard
import com.hilingual.presentation.voca.component.VocaEmptyCard
import com.hilingual.presentation.voca.component.VocaEmptyCardType
import com.hilingual.presentation.voca.component.VocaHeader
import com.hilingual.presentation.voca.component.VocaInfo
import com.hilingual.presentation.voca.component.VocaModal
import com.hilingual.presentation.voca.component.WordSortBottomSheet
import com.hilingual.presentation.voca.component.WordSortType
import com.hilingual.presentation.voca.model.VocaGroup
import com.hilingual.presentation.voca.model.VocaItem
import com.hilingual.presentation.voca.model.VocaItemDetail
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun VocaRoute(
    paddingValues: PaddingValues,
    viewModel: VocaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val localSystemBarsColor = LocalSystemBarsColor.current

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = hilingualBlack,
            isDarkIcon = false
        )
    }

    VocaScreen(
        paddingValues = paddingValues,
        viewType = uiState.viewType,
        sortType = uiState.sortType,
        vocaCount = uiState.vocaCount,
        vocaDetail = (uiState.vocaItemDetail as? UiState.Success)?.data,
        vocaGroupList = (uiState.vocaGroupList as? UiState.Success)?.data ?: persistentListOf(),
        searchResultList = (uiState.searchResultList as? UiState.Success)?.data
            ?: persistentListOf(),
        searchText = uiState.searchKeyword,
        onSortTypeChanged = viewModel::fetchWords,
        onCardClick = viewModel::fetchVocaDetail,
        onBookmarkClick = viewModel::toggleBookmark,
        onDismissModal = viewModel::clearSelectedVocaDetail,
        onSearchTextChanged = {
            viewModel.updateSearchKeywordAndSearch(searchKeyword = it)
        },
        onWriteDiaryClick = { /*TODO:네비게이션 연결해야해용*/ },
        onCloseButtonClick = viewModel::clearSearchKeyword
    )
}

@Composable
private fun VocaScreen(
    paddingValues: PaddingValues,
    viewType: ScreenType,
    sortType: WordSortType,
    vocaDetail: VocaItemDetail?,
    vocaCount: Int,
    vocaGroupList: ImmutableList<VocaGroup>,
    searchResultList: ImmutableList<VocaItem>,
    searchText: String,
    onWriteDiaryClick: () -> Unit,
    onSortTypeChanged: (WordSortType) -> Unit,
    onCardClick: (Int) -> Unit,
    onBookmarkClick: (Int) -> Unit,
    onDismissModal: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onCloseButtonClick: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.gray100)
            .padding(paddingValues)
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
                    searchResultList = searchResultList,
                    onCardClick = onCardClick,
                    onBookmarkClick = onBookmarkClick
                )
            }
        }
    }

    if (vocaDetail != null) {
        Box(
            modifier = Modifier
                .background(HilingualTheme.colors.dim)
                .clickable { onDismissModal() }
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            VocaModal(
                phrase = vocaDetail.phrase,
                phraseType = vocaDetail.phraseType.toImmutableList(),
                explanation = vocaDetail.explanation,
                createdAt = vocaDetail.createdAt,
                isBookmarked = vocaDetail.isBookmarked,
                onBookmarkClick = { onBookmarkClick(vocaDetail.phraseId) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }

    if (showBottomSheet) {
        WordSortBottomSheet(
            selectedType = sortType,
            onDismiss = { showBottomSheet = false },
            onTypeSelected = {
                onSortTypeChanged(it)
            }
        )
    }
}


@Composable
internal fun VocaListWithInfoSection(
    vocaGroupList: ImmutableList<VocaGroup>,
    sortType: WordSortType,
    wordCount: Int,
    onWriteDiaryClick: () -> Unit,
    onSortClick: () -> Unit,
    onCardClick: (Int) -> Unit,
    onBookmarkClick: (Int) -> Unit
) {
    val isEmpty = vocaGroupList.all { it.words.isEmpty() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            VocaInfo(
                wordCount = wordCount,
                sortType = sortType,
                onSortClick = onSortClick,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(if (isEmpty) 80.dp else 16.dp))
        }

        if (isEmpty) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VocaEmptyCard(type = VocaEmptyCardType.NOT_ADD)
                    Spacer(modifier = Modifier.height(16.dp))
                    AddVocaButton(onClick = onWriteDiaryClick)
                }
            }
        } else {
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
                            phraseType = voca.phraseType.toImmutableList(),
                            onCardClick = { onCardClick(voca.phraseId) },
                            isBookmarked = voca.isBookmarked,
                            onBookmarkClick = { onBookmarkClick(voca.phraseId) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (isLastItem) 20.dp else 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun SearchResultSection(
    searchResultList: ImmutableList<VocaItem>,
    onCardClick: (Int) -> Unit,
    onBookmarkClick: (Int) -> Unit,
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
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(
                searchResultList,
                key = { voca -> voca.phraseId }
            ) { voca ->
                VocaCard(
                    phrase = voca.phrase,
                    phraseType = voca.phraseType.toImmutableList(),
                    onCardClick = { onCardClick(voca.phraseId) },
                    isBookmarked = voca.isBookmarked,
                    onBookmarkClick = { onBookmarkClick(voca.phraseId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun VocaListWithInfoSectionEmptyPreview() {
    HilingualTheme {
        VocaListWithInfoSection(
            vocaGroupList = persistentListOf(
            ),
            sortType = WordSortType.Latest,
            wordCount = 0,
            onWriteDiaryClick = {},
            onSortClick = {},
            onCardClick = {},
            onBookmarkClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultSectionEmptyPreview() {
    HilingualTheme {
        SearchResultSection(
            searchResultList = persistentListOf(),
            onCardClick = {},
            onBookmarkClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultSectionListPreview() {
    HilingualTheme {
        SearchResultSection(
            searchResultList = persistentListOf(
                VocaItem(
                    phraseId = 1,
                    phrase = "healing",
                    phraseType = persistentListOf("명사"),
                    isBookmarked = true
                ),
                VocaItem(
                    phraseId = 2,
                    phrase = "relax",
                    phraseType = persistentListOf("동사"),
                    isBookmarked = true
                )
            ).toImmutableList(),
            onCardClick = {},
            onBookmarkClick = {}
        )
    }
}

