package com.example.buy_it.ui.screens.revieweditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buy_it.data.repository.AuthRepository
import com.example.buy_it.data.repository.ProductRepository
import com.example.buy_it.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ReviewEditorViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewEditorState())
    val uiState: StateFlow<ReviewEditorState> = _uiState.asStateFlow()

    private val currentUserId: String?
        get() = authRepository.currentUser?.uid

    private fun recalculate(state: ReviewEditorState): ReviewEditorState {
        val baseCanPublish = state.opinion.trim().isNotEmpty()

        val finalCanPublish = if (state.isEditMode) {
            baseCanPublish && state.canEditOrDelete
        } else {
            baseCanPublish
        }

        return state.copy(
            canPublish = finalCanPublish
        )
    }

    fun onOpinionChange(opinion: String) {
        if (_uiState.value.isEditMode && !_uiState.value.canEditOrDelete) return

        _uiState.update { current ->
            recalculate(current.copy(opinion = opinion))
        }
    }

    fun loadEditor(productId: String, reviewId: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val productResult = productRepository.getProductById(productId)

            if (productResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = productResult.exceptionOrNull()?.message ?: "Error al cargar el producto"
                    )
                }
                return@launch
            }

            val product = productResult.getOrThrow()

            _uiState.update {
                recalculate(
                    it.copy(
                        productName = product.name,
                        productImage = product.image,
                        isLoading = false,
                        errorMessage = null
                    )
                )
            }

            if (!reviewId.isNullOrBlank()) {
                loadReviewForEdit(reviewId)
            }
        }
    }

    private fun loadReviewForEdit(reviewId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val result = reviewRepository.getReviewById(reviewId)

            if (result.isSuccess) {
                val review = result.getOrThrow()
                val isOwner = review.userId == currentUserId

                _uiState.update {
                    recalculate(
                        it.copy(
                            reviewId = review.id,
                            likeChoice = if (review.like) LikeChoice.Like else LikeChoice.Dislike,
                            opinion = review.review,
                            isLoading = false,
                            errorMessage = if (isOwner) null else "No puedes editar ni eliminar la reseña de otro usuario.",
                            isEditMode = true,
                            canEditOrDelete = isOwner,
                            navigateBack = !isOwner
                        )
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error al cargar la reseña"
                    )
                }
            }
        }
    }

    fun submitReview(productId: String) {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.isEditMode && !state.canEditOrDelete) {
                _uiState.update {
                    recalculate(
                        it.copy(
                            isLoading = false,
                            errorMessage = "No puedes editar una reseña de otro usuario."
                        )
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, navigateBack = false)
            }

            val likeValue = state.likeChoice == LikeChoice.Like

            val result = if (state.reviewId == null) {
                reviewRepository.createReview(
                    productId = productId,
                    like = likeValue,
                    comment = state.opinion.trim()
                )
            } else {
                reviewRepository.updateReview(
                    id = state.reviewId,
                    productId = productId,
                    like = likeValue,
                    comment = state.opinion.trim()
                )
            }

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = null, navigateBack = true)
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error al guardar la reseña"
                    )
                }
            }
        }
    }

    fun deleteReview() {
        val state = _uiState.value
        val reviewId = state.reviewId ?: return

        if (!state.canEditOrDelete) {
            _uiState.update {
                recalculate(
                    it.copy(
                        isLoading = false,
                        errorMessage = "No puedes eliminar una reseña de otro usuario."
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, navigateBack = false)
            }

            val result = reviewRepository.deleteReview(reviewId)

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = null, navigateBack = true)
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error al eliminar la reseña"
                    )
                }
            }
        }
    }

    fun onNavigatedBack() {
        _uiState.update { it.copy(navigateBack = false) }
    }
}