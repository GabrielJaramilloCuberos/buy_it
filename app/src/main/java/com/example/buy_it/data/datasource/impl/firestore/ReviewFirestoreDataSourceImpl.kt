package com.example.buy_it.data.datasource.impl.firestore

import com.example.buy_it.data.datasource.ReviewRemoteDataSource
import com.example.buy_it.data.dtos.CreateReviewDto
import com.example.buy_it.data.dtos.ReviewDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.FieldValue
import com.example.buy_it.data.dtos.UserDTO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewRemoteDataSource {

    override suspend fun getAllReviews(): List<ReviewDTO> {
        val snapshot = db.collection("reviews").get().await()

        return snapshot.documents.mapNotNull { doc ->
            mapReviewDocument(doc)
        }
    }

    override suspend fun getReviewById(id: String): ReviewDTO {
        val doc = db.collection("reviews").document(id).get().await()
        val dto = doc.toObject(ReviewDTO::class.java)
            ?: throw Exception("Review no encontrada")
        return dto.copy(id = doc.id)
    }

    override suspend fun getReviewsByUserId(userId: String): List<ReviewDTO> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            mapReviewDocument(doc)
        }
    }

    override suspend fun getReviewsByProductId(productId: String): List<ReviewDTO> {
        return getReviewsByProductId(
            productId = productId,
            currentUserId = null
        )
    }

    override suspend fun getReviewsByProductId(
        productId: String,
        currentUserId: String?
    ): List<ReviewDTO> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("productId", productId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            mapReviewDocument(doc, currentUserId)
        }
    }

    override suspend fun createReview(review: CreateReviewDto) {
        db.collection("reviews")
            .add(review)
            .await()
    }

    override suspend fun updateReview(
        id: String,
        review: CreateReviewDto
    ) {
        db.collection("reviews")
            .document(id)
            .set(review)
            .await()
    }

    override suspend fun deleteReview(id: String) {
        db.collection("reviews")
            .document(id)
            .delete()
            .await()
    }

    override suspend fun sendReviewLike(reviewId: String, userId: String) {
        val reviewRef = db.collection("reviews").document(reviewId)
        val likeRef = reviewRef.collection("likes").document(userId)

        db.runTransaction { transaction ->
            val likeDoc = transaction.get(likeRef)

            if (likeDoc.exists()) {
                transaction.delete(likeRef)
                transaction.update(reviewRef, "likesCount", FieldValue.increment(-1))
            } else {
                transaction.set(
                    likeRef,
                    mapOf("createdAt" to FieldValue.serverTimestamp())
                )
                transaction.update(reviewRef, "likesCount", FieldValue.increment(1))
            }
        }.await()
    }

    private suspend fun mapReviewDocument(
        doc: com.google.firebase.firestore.DocumentSnapshot,
        currentUserId: String? = null
    ): ReviewDTO? {
        val baseDto = doc.toObject(ReviewDTO::class.java)?.copy(id = doc.id)
            ?: return null

        val userDto = if (baseDto.userId.isNotBlank()) {
            db.collection("users")
                .document(baseDto.userId)
                .get()
                .await()
                .toObject(UserDTO::class.java)
        } else {
            null
        }

        val likedByCurrentUser = if (!currentUserId.isNullOrBlank()) {
            db.collection("reviews")
                .document(doc.id)
                .collection("likes")
                .document(currentUserId)
                .get()
                .await()
                .exists()
        } else {
            false
        }

        return baseDto.copy(
            user = userDto,
            likedByCurrentUser = likedByCurrentUser
        )
    }

    override suspend fun getReviewsByUserIds(userIds: List<String>): List<ReviewDTO> {
        if (userIds.isEmpty()) return emptyList()

        val result = mutableListOf<ReviewDTO>()

        userIds.chunked(10).forEach { chunk ->
            val snapshot = db.collection("reviews")
                .whereIn("userId", chunk)
                .get()
                .await()

            val reviews = snapshot.documents.mapNotNull { doc ->
                mapReviewDocument(doc)
            }

            result.addAll(reviews)
        }

        return result
    }

    override fun listenReviewsByProductId(
        productId: String,
        currentUserId: String?
    ): Flow<List<ReviewDTO>> = callbackFlow {
        val listener = db.collection("reviews")
            .whereEqualTo("productId", productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    launch {
                        val reviews = snapshot.documents.mapNotNull { doc ->
                            mapReviewDocument(doc, currentUserId)
                        }

                        trySend(reviews)
                    }
                }
            }

        awaitClose {
            listener.remove()
        }
    }
}