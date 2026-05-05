package com.example.buy_it.data.dtos

data class CreateReviewDto(
    val userId: String = "",
    val productId: String = "",
    val like: Boolean = false,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val product: ProductDTO? = null
)