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
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualBlack
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
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun VocaRoute(
    paddingValues: PaddingValues,
    viewModel: VocaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val vocaDetail by viewModel.selectedVocaDetail.collectAsStateWithLifecycle()
    val selectedVocaItem by viewModel.selectedVocaItem.collectAsStateWithLifecycle()
    val systemUiController = rememberSystemUiController()

    var searchText by remember { mutableStateOf("") }
    var isSearch by remember { mutableStateOf(false) }
    var searchResultList by remember { mutableStateOf(emptyList<VocaItem>()) }

    SideEffect {
        systemUiController.setStatusBarColor(hilingualBlack, darkIcons = false)
    }

    when (val state = uiState) {
        is UiState.Loading -> {}
        is UiState.Success -> {
            VocaScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                vocaDetail = vocaDetail,
                selectedVocaItem = selectedVocaItem,
                isSearch = isSearch,
                searchResultList = searchResultList.toImmutableList(),
                searchText = searchText,
                onSortTypeChanged = viewModel::fetchWords,
                onCardClick = viewModel::fetchVocaDetail,
                onBookmarkClick = viewModel::toggleBookmark,
                onDismissModal = viewModel::clearSelectedItem,
                onSearchTextChanged = {
                    searchText = it
                    if (it.isBlank()) {
                        isSearch = false
                        searchResultList = emptyList()
                    }
                },
                onSearchConfirmed = {
                    if (searchText.isNotBlank()) {
                        isSearch = true
                        searchResultList = state.data.vocaGroupList
                            .flatMap { it.words }
                            .filter { it.phrase.startsWith(searchText, ignoreCase = true) }
                            .sortedBy { it.phrase }
                    } else {
                        isSearch = false
                        searchResultList = emptyList()
                    }
                }
            )
        }

        else -> {}
    }
}

@Composable
private fun VocaScreen(
    paddingValues: PaddingValues,
    uiState: VocaUiState,
    vocaDetail: VocaItemDetail?,
    selectedVocaItem: VocaItem?,
    isSearch: Boolean,
    searchResultList: ImmutableList<VocaItem>,
    searchText: String,
    onSortTypeChanged: (WordSortType) -> Unit,
    onCardClick: (VocaItem) -> Unit,
    onBookmarkClick: (VocaItem) -> Unit,
    onDismissModal: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSearchConfirmed: () -> Unit
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
            onSearchConfirmed = onSearchConfirmed,
            modifier = Modifier
                .background(hilingualBlack)
                .fillMaxWidth()
        )

        if (isSearch) {
            SearchResultSection(
                searchResultList = searchResultList,
                onCardClick = onCardClick,
                onBookmarkClick = onBookmarkClick
            )
        } else {
            VocaListWithInfoSection(
                vocaGroupList = uiState.vocaGroupList.toImmutableList(),
                sortType = uiState.sortType,
                wordCount = uiState.vocaGroupList.sumOf { it.words.size },
                onSortClick = { showBottomSheet = true },
                onCardClick = onCardClick,
                onBookmarkClick = onBookmarkClick
            )
        }
    }

    if (selectedVocaItem != null && vocaDetail != null) {
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
                isBookmarked = selectedVocaItem.isBookmarked,
                onBookmarkClick = { onBookmarkClick(selectedVocaItem) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }

    if (showBottomSheet) {
        WordSortBottomSheet(
            selectedType = uiState.sortType,
            onDismiss = { showBottomSheet = false },
            onTypeSelected = {
                onSortTypeChanged(it)
                showBottomSheet = false
            }
        )
    }
}


@Composable
internal fun VocaListWithInfoSection(
    vocaGroupList: ImmutableList<VocaGroup>,
    sortType: WordSortType,
    wordCount: Int,
    onSortClick: () -> Unit,
    onCardClick: (VocaItem) -> Unit,
    onBookmarkClick: (VocaItem) -> Unit
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
                    AddVocaButton(onClick = { /* TODO: 단어 추가해용 */ })
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

                    itemsIndexed(group.words) { index, voca ->
                        val isLastItem = index == group.words.lastIndex
                        VocaCard(
                            phrase = voca.phrase,
                            phraseType = voca.phraseType.toImmutableList(),
                            onCardClick = { onCardClick(voca) },
                            isBookmarked = voca.isBookmarked,
                            onBookmarkClick = { onBookmarkClick(voca) },
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
    searchResultList: List<VocaItem>,
    onCardClick: (VocaItem) -> Unit,
    onBookmarkClick: (VocaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        if (searchResultList.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(horizontal = 80.dp)
                        .padding(top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VocaEmptyCard(type = VocaEmptyCardType.NOT_SEARCH)
                }
            }
        } else {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(searchResultList) { voca ->
                VocaCard(
                    phrase = voca.phrase,
                    phraseType = voca.phraseType.toImmutableList(),
                    onCardClick = { onCardClick(voca) },
                    isBookmarked = voca.isBookmarked,
                    onBookmarkClick = { onBookmarkClick(voca) },
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
fun SearchResultSectionPreview() {
    val sampleVocaItems = remember {
        listOf(
            VocaItem(
                1,
                phrase = "apple",
                phraseType = listOf("명사"),
                isBookmarked = true
            ),
            VocaItem(
                2,
                phrase = "application",
                phraseType = listOf("명사"),
                isBookmarked = true
            ),
            VocaItem(
                3,
                phrase = "appreciate",
                phraseType = listOf("동사"),
                isBookmarked = true
            ),
            VocaItem(
                4,
                phrase = "anana",
                phraseType = listOf("명사"),
                isBookmarked = true
            )
        )
    }

    HilingualTheme {
        SearchResultSection(
            searchResultList = sampleVocaItems,
            onCardClick = {},
            onBookmarkClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultSectionEmptyPreview() {
    HilingualTheme {
        SearchResultSection(
            searchResultList = emptyList(),
            onCardClick = {},
            onBookmarkClick = {}
        )
    }
}
