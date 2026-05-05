package com.example.buy_it.data.datasource.impl.firestore

import android.util.Log
import com.example.buy_it.data.datasource.UserRemoteDatasource
import com.example.buy_it.data.dtos.RegisterUserDto
import com.example.buy_it.data.dtos.ReviewDTO
import com.example.buy_it.data.dtos.UserProfileFirestoreDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.FieldValue
import com.example.buy_it.data.dtos.UserDTO

class UserFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDatasource {

    override suspend fun getUserById(id: String): UserProfileFirestoreDTO? {
        return getUserById(id, null)
    }

    override suspend fun getUserById(
        id: String,
        currentUserId: String?
    ): UserProfileFirestoreDTO? {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()

        val user = respuesta.toObject(UserProfileFirestoreDTO::class.java)
            ?: return null

        val userWithId = user.copy(id = respuesta.id)

        if (currentUserId.isNullOrBlank() || currentUserId == id) {
            return userWithId
        }

        val followDoc = db.collection("users")
            .document(id)
            .collection("followers")
            .document(currentUserId)
            .get()
            .await()

        userWithId.followed = followDoc.exists()

        return userWithId
    }

    override suspend fun getUserReviews(id: String): List<ReviewDTO> {
        return try {
            val snapshot = db.collectionGroup("reviews")
                .whereEqualTo("userId", id)
                .get()
                .await()

            snapshot.toObjects(ReviewDTO::class.java)
        } catch (e: Exception) {
            Log.e("UserReviews", "Error al obtener reseñas del usuario: ${e.message}")
            emptyList()
        }
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.set(registerUserDto).await()
    }

    override suspend fun updateUserProfile(userId: String, name: String, pfpURL: String?) {
        val updates = mutableMapOf<String, Any>(
            "name" to name
        )

        if (!pfpURL.isNullOrBlank()) {
            updates["pfpURL"] = pfpURL
        }

        db.collection("users")
            .document(userId)
            .set(updates, SetOptions.merge())
            .await()
    }

    override suspend fun followOrUnfollowUser(
        currentUserId: String,
        targetUserId: String
    ) {
        if (currentUserId.isBlank() || targetUserId.isBlank()) {
            throw Exception("Usuario inválido")
        }

        if (currentUserId == targetUserId) {
            throw Exception("No puedes seguirte a ti mismo")
        }

        val currentUserRef = db.collection("users").document(currentUserId)
        val targetUserRef = db.collection("users").document(targetUserId)

        val followingRef = currentUserRef
            .collection("following")
            .document(targetUserId)

        val followersRef = targetUserRef
            .collection("followers")
            .document(currentUserId)

        db.runTransaction { transaction ->
            val followingDoc = transaction.get(followingRef)

            if (followingDoc.exists()) {
                transaction.delete(followingRef)
                transaction.delete(followersRef)

                transaction.update(
                    currentUserRef,
                    "followingCount",
                    FieldValue.increment(-1)
                )

                transaction.update(
                    targetUserRef,
                    "followersCount",
                    FieldValue.increment(-1)
                )
            } else {
                val data = mapOf(
                    "createdAt" to FieldValue.serverTimestamp()
                )

                transaction.set(followingRef, data)
                transaction.set(followersRef, data)

                transaction.update(
                    currentUserRef,
                    "followingCount",
                    FieldValue.increment(1)
                )

                transaction.update(
                    targetUserRef,
                    "followersCount",
                    FieldValue.increment(1)
                )
            }
        }.await()
    }

    override suspend fun getFollowingIds(userId: String): List<String> {
        val snapshot = db.collection("users")
            .document(userId)
            .collection("following")
            .get()
            .await()

        return snapshot.documents.map { it.id }
    }

    override suspend fun getFollowerIds(userId: String): List<String> {
        val snapshot = db.collection("users")
            .document(userId)
            .collection("followers")
            .get()
            .await()

        return snapshot.documents.map { it.id }
    }

    override suspend fun getUsersByIds(userIds: List<String>): List<UserDTO> {
        if (userIds.isEmpty()) return emptyList()

        val users = mutableListOf<UserDTO>()

        userIds.forEach { userId ->
            val doc = db.collection("users")
                .document(userId)
                .get()
                .await()

            val user = doc.toObject(UserDTO::class.java)

            if (user != null) {
                users.add(
                    user.copy(id = doc.id)
                )
            }
        }

        return users
    }
}