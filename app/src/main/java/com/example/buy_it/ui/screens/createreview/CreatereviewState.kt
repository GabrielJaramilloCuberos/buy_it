package com.example.buy_it.ui.screens.createreview

data class CreateReviewState(
    val productName: String = "",
    val comment: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)