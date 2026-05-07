package com.example.buy_it.ui.screens.revieweditor

enum class LikeChoice {
    None,
    Like,
    Dislike
}

data class ReviewEditorState(
    val reviewId: String? = null,
    val productName: String = "",
    val productImage: String = "",
    val likeChoice: LikeChoice = LikeChoice.Like,
    val opinion: String = "",
    val canPublish: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigateBack: Boolean = false,
    val isEditMode: Boolean = false,
    val canEditOrDelete: Boolean = true,
)