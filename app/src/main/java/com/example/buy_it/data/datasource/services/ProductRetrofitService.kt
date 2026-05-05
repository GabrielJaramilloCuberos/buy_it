package com.example.buy_it.data.datasource.services

import com.example.buy_it.data.dtos.CreateProductDto
import com.example.buy_it.data.dtos.ProductDTO
import com.example.buy_it.data.dtos.ReviewDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductRetrofitService {

    @GET("products")
    suspend fun getAllProducts(): List<ProductDTO>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: String): ProductDTO

    @GET("products/{id}/reviews")
    suspend fun getProductReviews(@Path("id") id: String): List<ReviewDTO>

    @POST("products")
    suspend fun createProduct(@Body tweet: CreateProductDto): Unit

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body tweet: CreateProductDto)

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Unit

}