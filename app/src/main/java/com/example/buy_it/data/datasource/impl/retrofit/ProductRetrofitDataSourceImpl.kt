package com.example.buy_it.data.datasource.impl.retrofit

import com.example.buy_it.data.datasource.ProductRemoteDataSource
import com.example.buy_it.data.datasource.services.ProductRetrofitService
import com.example.buy_it.data.dtos.CreateProductDto
import com.example.buy_it.data.dtos.PriceDTO
import com.example.buy_it.data.dtos.ProductDTO
import com.example.buy_it.data.dtos.ReviewDTO
import javax.inject.Inject

class ProductRetrofitDataSourceImpl @Inject constructor(
    val service: ProductRetrofitService
): ProductRemoteDataSource {
    override suspend fun getAllProducts(): List<ProductDTO> {
        return service.getAllProducts()
    }

    override suspend fun getProduct(id: String): ProductDTO {
        return service.getProduct(id)
    }

    override suspend fun getProductReviews(id: String): List<ReviewDTO> {
        return service.getProductReviews(id)
    }

    override suspend fun getProductPrices(id: String): List<PriceDTO> {
        TODO("Aun no esta implementado, toca ver esto como se haria en una relacional jijijija")
    }

    override suspend fun createProduct(tweet: CreateProductDto) {
        return service.createProduct(tweet)
    }

    override suspend fun deleteProduct(id: String) {
        return service.deleteProduct(id)
    }

    override suspend fun updateProduct(
        id: String,
        tweet: CreateProductDto
    ) {
        return service.updateProduct(id, tweet)
    }
}