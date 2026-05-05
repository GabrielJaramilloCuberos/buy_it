package com.example.buy_it.data.datasource.services

import com.example.buy_it.data.dtos.CreateReviewDto
import com.example.buy_it.data.dtos.ReviewDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ReviewRetrofitService {

    @GET("reviews")
    suspend fun getAllReviews(): List<ReviewDTO>

    @GET("reviews/{id}")
    suspend fun getReviewById(@Path("id") id: String): ReviewDTO

    @POST("reviews")
    suspend fun createReview(@Body review: CreateReviewDto)

    @PUT("reviews/{id}")
    suspend fun updateReview(@Path("id") id: String, @Body review: CreateReviewDto)

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String)

    @GET("users/{userId}/reviews")
    suspend fun getReviewsByUserId(@Path("userId") userId: String): List<ReviewDTO>

    @GET("products/{id}/reviews")
    suspend fun getProductReviews(@Path("id") id: String): List<ReviewDTO>
}