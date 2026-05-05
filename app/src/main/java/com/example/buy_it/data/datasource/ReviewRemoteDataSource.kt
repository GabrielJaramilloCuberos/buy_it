package com.example.buy_it.data.datasource

import com.example.buy_it.data.dtos.CreateReviewDto
import com.example.buy_it.data.dtos.ReviewDTO
import kotlinx.coroutines.flow.Flow
interface ReviewRemoteDataSource {

    suspend fun getAllReviews(): List<ReviewDTO>

    suspend fun getReviewById(id: String): ReviewDTO

    suspend fun getReviewsByUserId(userId: String): List<ReviewDTO>

    suspend fun getReviewsByProductId(productId: String): List<ReviewDTO>

    suspend fun createReview(review: CreateReviewDto)

    suspend fun updateReview(id: String, review: CreateReviewDto)

    suspend fun deleteReview(id: String)

    suspend fun sendReviewLike(reviewId: String, userId: String)

    suspend fun getReviewsByProductId(
        productId: String,
        currentUserId: String?
    ): List<ReviewDTO>

    suspend fun getReviewsByUserIds(userIds: List<String>): List<ReviewDTO>

    fun listenReviewsByProductId(
        productId: String,
        currentUserId: String?
    ): Flow<List<ReviewDTO>>
}