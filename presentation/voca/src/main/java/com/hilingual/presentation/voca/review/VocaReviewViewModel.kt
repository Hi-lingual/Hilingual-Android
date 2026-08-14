package com.hilingual.presentation.voca.review

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.util.UiState
import com.hilingual.data.voca.model.VocaMemorizationModel
import com.hilingual.data.voca.repository.VocaRepository
import com.hilingual.presentation.voca.navigation.VocaReview
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class VocaReviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val vocaRepository: VocaRepository,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<VocaReview>()

    private val _uiState = MutableStateFlow(VocaReviewUiState())
    val uiState: StateFlow<VocaReviewUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<VocaReviewSideEffect>()
    val sideEffect: SharedFlow<VocaReviewSideEffect> = _sideEffect.asSharedFlow()

    private val results = linkedMapOf<Long, Boolean>()

    init {
        restoreResults()
        fetchCards()
    }

    fun retryLoad() = fetchCards()

    private fun fetchCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(cards = UiState.Loading) }
            vocaRepository.getVocaList(sort = route.sort, unmemorizedOnly = route.unmemorizedOnly)
                .onSuccess { result ->
                    val cards = result.list
                        .flatMap { it.words }
                        .map { word ->
                            ReviewCardUiModel(
                                phraseId = word.phraseId,
                                phrase = word.phrase,
                                phraseType = word.phraseType.toImmutableList(),
                                explanation = word.explanation,
                            )
                        }
                        .toImmutableList()

                    if (cards.isEmpty()) {
                        _uiState.update { it.copy(exitReason = ReviewExitReason.EMPTY_DECK) }
                        return@launch
                    }

                    val firstUnjudgedIndex = cards.indexOfFirst { it.phraseId !in results }
                    if (firstUnjudgedIndex == -1) {
                        _uiState.update {
                            it.copy(
                                cards = UiState.Success(cards),
                                currentIndex = cards.lastIndex,
                                phase = ReviewPhase.COMPLETED,
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                cards = UiState.Success(cards),
                                currentIndex = firstUnjudgedIndex,
                            )
                        }
                    }
                }
                .onLogFailure {
                    _uiState.update { it.copy(cards = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    fun judgeCurrentCard(isMemorized: Boolean) {
        val state = _uiState.value
        val cards = (state.cards as? UiState.Success)?.data ?: return
        val currentCard = cards.getOrNull(state.currentIndex) ?: return

        results[currentCard.phraseId] = isMemorized
        persistResults()
    }

    fun moveToNextCard() {
        val state = _uiState.value
        val cards = (state.cards as? UiState.Success)?.data ?: return

        if (state.currentIndex == cards.lastIndex) {
            _uiState.update { it.copy(phase = ReviewPhase.COMPLETED) }
        } else {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
        }
    }

    fun onBackPressed() {
        when (_uiState.value.phase) {
            ReviewPhase.REVIEWING -> {
                if (results.isEmpty()) {
                    _uiState.update { it.copy(exitReason = ReviewExitReason.CANCELLED) }
                } else {
                    _uiState.update { it.copy(phase = ReviewPhase.EXIT_CONFIRM) }
                }
            }

            ReviewPhase.EXIT_CONFIRM -> _uiState.update { it.copy(phase = ReviewPhase.REVIEWING) }

            ReviewPhase.COMPLETED -> saveResults()
        }
    }

    fun exitWithoutSaving() {
        _uiState.update { it.copy(exitReason = ReviewExitReason.CANCELLED) }
    }

    fun saveResults() {
        if (_uiState.value.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            vocaRepository.patchVocaMemorization(
                items = results.map { (phraseId, isMemorized) ->
                    VocaMemorizationModel(phraseId = phraseId, isMemorized = isMemorized)
                },
            )
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, exitReason = ReviewExitReason.SAVED) }
                }
                .onLogFailure {
                    _uiState.update { it.copy(isSaving = false) }
                    _sideEffect.emit(VocaReviewSideEffect.ShowErrorDialog)
                }
        }
    }

    private fun restoreResults() {
        val judgedIds = savedStateHandle.get<LongArray>(KEY_JUDGED_IDS) ?: return
        val judgedValues = savedStateHandle.get<BooleanArray>(KEY_JUDGED_VALUES) ?: return
        if (judgedIds.size != judgedValues.size) return

        judgedIds.forEachIndexed { index, phraseId ->
            results[phraseId] = judgedValues[index]
        }
    }

    private fun persistResults() {
        savedStateHandle[KEY_JUDGED_IDS] = results.keys.toLongArray()
        savedStateHandle[KEY_JUDGED_VALUES] = results.values.toBooleanArray()
    }

    companion object {
        private const val KEY_JUDGED_IDS = "review_judged_ids"
        private const val KEY_JUDGED_VALUES = "review_judged_values"
    }
}

@Immutable
data class VocaReviewUiState(
    val cards: UiState<ImmutableList<ReviewCardUiModel>> = UiState.Loading,
    val currentIndex: Int = 0,
    val phase: ReviewPhase = ReviewPhase.REVIEWING,
    val isSaving: Boolean = false,
    val exitReason: ReviewExitReason? = null,
)

@Immutable
data class ReviewCardUiModel(
    val phraseId: Long,
    val phrase: String,
    val phraseType: ImmutableList<String>,
    val explanation: String,
)

enum class ReviewPhase {
    REVIEWING,
    EXIT_CONFIRM,
    COMPLETED,
}

enum class ReviewExitReason {
    EMPTY_DECK,
    CANCELLED,
    SAVED,
}

sealed interface VocaReviewSideEffect {
    data object ShowErrorDialog : VocaReviewSideEffect
}
