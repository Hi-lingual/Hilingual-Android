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
    savedStateHandle: SavedStateHandle,
    private val vocaRepository: VocaRepository,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<VocaReview>()

    private val _uiState = MutableStateFlow(VocaReviewUiState())
    val uiState: StateFlow<VocaReviewUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<VocaReviewSideEffect>()
    val sideEffect: SharedFlow<VocaReviewSideEffect> = _sideEffect.asSharedFlow()

    private val results = linkedMapOf<Long, Boolean>()

    init {
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
                        _sideEffect.emit(VocaReviewSideEffect.NavigateUp)
                        return@launch
                    }
                    _uiState.update { it.copy(cards = UiState.Success(cards)) }
                }
                .onLogFailure {
                    _uiState.update { it.copy(cards = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    fun selectResult(isMemorized: Boolean) {
        val state = _uiState.value
        val cards = (state.cards as? UiState.Success)?.data ?: return
        val currentCard = cards.getOrNull(state.currentIndex) ?: return

        results[currentCard.phraseId] = isMemorized

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
                    viewModelScope.launch { _sideEffect.emit(VocaReviewSideEffect.NavigateUp) }
                } else {
                    _uiState.update { it.copy(phase = ReviewPhase.EXIT_CONFIRM) }
                }
            }

            ReviewPhase.EXIT_CONFIRM -> _uiState.update { it.copy(phase = ReviewPhase.REVIEWING) }

            ReviewPhase.COMPLETED -> saveResults()
        }
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
                    _uiState.update { it.copy(isSaving = false) }
                    _sideEffect.emit(VocaReviewSideEffect.NavigateUpWithSaved)
                }
                .onLogFailure {
                    _uiState.update { it.copy(isSaving = false) }
                    _sideEffect.emit(VocaReviewSideEffect.ShowErrorDialog)
                }
        }
    }
}

@Immutable
data class VocaReviewUiState(
    val cards: UiState<ImmutableList<ReviewCardUiModel>> = UiState.Loading,
    val currentIndex: Int = 0,
    val phase: ReviewPhase = ReviewPhase.REVIEWING,
    val isSaving: Boolean = false,
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

sealed interface VocaReviewSideEffect {
    data object NavigateUp : VocaReviewSideEffect
    data object NavigateUpWithSaved : VocaReviewSideEffect
    data object ShowErrorDialog : VocaReviewSideEffect
}
