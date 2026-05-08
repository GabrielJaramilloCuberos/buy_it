package com.example.buy_it.data.repository

import android.util.Log
import coil.network.HttpException
import com.example.buy_it.data.PricedItems
import com.example.buy_it.data.ProductInfo
import com.example.buy_it.data.ReviewInfo
import com.example.buy_it.data.datasource.impl.firestore.ProductFirestoreDataSourceImpl
import com.example.buy_it.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.buy_it.data.datasource.local.FirestoreSeedProducts
import com.example.buy_it.data.dtos.CreateProductDto
import com.example.buy_it.data.dtos.toProductInfo
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productRemoteDataSource: ProductFirestoreDataSourceImpl,
    private val userRemoteDataSource: UserFirestoreDataSourceImpl,
    private val reviewRepository: ReviewRepository
) {

    suspend fun seedFirestoreProductsIfNeeded(): Result<Unit> {
        return try {
            val currentProducts = productRemoteDataSource.getAllProducts()

            if (currentProducts.size >= 10) {
                return Result.success(Unit)
            }

            val existingKeys = currentProducts
                .map { "${it.name.trim().lowercase()}|${it.brand.trim().lowercase()}" }
                .toMutableSet()

            FirestoreSeedProducts.items.forEach { product ->
                val key = "${product.name.trim().lowercase()}|${product.brand.trim().lowercase()}"
                if (!existingKeys.contains(key)) {
                    productRemoteDataSource.createProduct(product)
                    existingKeys.add(key)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("prods", "ERROR seed Firestore: $e")
            Result.failure(e)
        }
    }

    suspend fun getAllProducts(): Result<List<ProductInfo>> {
        return try {
            val products = productRemoteDataSource.getAllProducts()
            val productsInfo = products.map { dto ->
                val reviews = reviewRepository.getReviewsByProductId(dto.id).getOrDefault(emptyList())
                Log.d("prods, ", "Id: ${dto.id}")
                Log.d("prods", "Reviews: $reviews")
                dto.toProductInfo().copy(ratingsCount = reviews.size)
            }
            Log.d("prods", "Productos: $productsInfo")
            Result.success(productsInfo)
        } catch (e: HttpException) {
            Log.d("prods", "ERROR http: $e")
            Result.failure(e)
        } catch (e: Exception) {
            Log.d("prods", "ERROR: $e")
            Result.failure(e)
        }
    }

    suspend fun createProduct(
        name: String,
        brand: String,
        imageUrl: String?,
        description: String?,
        range: String?
    ): Result<Unit> {
        return try {
            val createProductDTO = CreateProductDto(name, brand, imageUrl, description, range)
            productRemoteDataSource.createProduct(createProductDTO)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Log.d("prods", "Error getting product reviews: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: String): Result<ProductInfo> {
        return try {
            val productDTO = productRemoteDataSource.getProduct(id)
            val reviews = reviewRepository.getReviewsByProductId(id).getOrDefault(emptyList())
            Result.success(productDTO.toProductInfo().copy(ratingsCount = reviews.size))
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Log.d("prods", "Error getting product reviews: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getProductReviews(id: String): Result<List<ReviewInfo>> {
        return try {
            val reviewInfos = reviewRepository.getReviewsByProductId(id).getOrDefault(emptyList())

            val completedReviews = reviewInfos.map { info ->
                if (info.name == "Usuario desconocido") {
                    try {
                        val userDto = userRemoteDataSource.getUserById(info.userId)
                        val userInfo = userDto?.toUserProfileInfo()

                        if (userInfo != null) {
                            info.copy(
                                name = userInfo.name,
                                profileImage = userInfo.pfpURL
                            )
                        } else {
                            info
                        }
                    } catch (e: Exception) {
                        info
                    }
                } else {
                    info
                }
            }

            Result.success(completedReviews)
        } catch (e: Exception) {
            Log.d("prods", "Error getting product reviews: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getProductPrices(id: String): Result<List<PricedItems>> {
        return try {
            val priceDTOs = productRemoteDataSource.getProductPrices(id)
            val pricedItems = priceDTOs.map { dto ->
                PricedItems(
                    name = dto.storeName,
                    price = dto.price,
                    image = dto.storeLogo,
                    percentage = dto.percentage
                )
            }
            Result.success(pricedItems)
        } catch (e: Exception) {
            Log.d("prods", "Error getting product reviews: ${e.message}")
            Result.failure(e)
        }
    }
}