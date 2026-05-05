package com.example.buy_it.data.datasource.impl.firestore

import com.example.buy_it.data.datasource.ProductRemoteDataSource
import com.example.buy_it.data.dtos.CreateProductDto
import com.example.buy_it.data.dtos.ProductDTO
import com.example.buy_it.data.dtos.ReviewDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ProductRemoteDataSource {

    override suspend fun getAllProducts(): List<ProductDTO> {
        val snapshot = db.collection("products").get().await()

        return snapshot.documents.map { document ->
            val dto = document.toObject(ProductDTO::class.java) ?: ProductDTO()
            dto.copy(id = document.id)
        }
    }

    override suspend fun getProduct(id: String): ProductDTO {
        val docRef = db.collection("products").document(id)
        val respuesta = docRef.get().await()

        val dto = respuesta.toObject(ProductDTO::class.java)
            ?: throw Exception("Producto no encontrado.")

        return dto.copy(id = respuesta.id)
    }

    override suspend fun getProductReviews(id: String): List<ReviewDTO> {
        return try {
            val snapshot = db.collection("products")
                .document(id)
                .collection("reviews")
                .get()
                .await()

            snapshot.toObjects(ReviewDTO::class.java)
        } catch (e: Exception) {
            throw Exception("Error al obtener reseñas: ${e.message}")
        }
    }

    override suspend fun createProduct(tweet: CreateProductDto) {
        val productData = hashMapOf(
            "name" to tweet.name,
            "brand" to tweet.brand,
            "imageURL" to (tweet.imageUrl ?: ""),
            "description" to (tweet.description ?: ""),
            "percentageLike" to 0,
            "range" to (tweet.range ?: ""),
            "created" to System.currentTimeMillis().toString()
        )

        db.collection("products")
            .add(productData)
            .await()
    }

    override suspend fun deleteProduct(id: String) {
        db.collection("products")
            .document(id)
            .delete()
            .await()
    }

    override suspend fun updateProduct(
        id: String,
        tweet: CreateProductDto
    ) {
        val productData = hashMapOf(
            "name" to tweet.name,
            "brand" to tweet.brand,
            "imageURL" to (tweet.imageUrl ?: ""),
            "description" to (tweet.description ?: ""),
            "range" to (tweet.range ?: "")
        )

        db.collection("products")
            .document(id)
            .set(productData, SetOptions.merge())
            .await()
    }
}