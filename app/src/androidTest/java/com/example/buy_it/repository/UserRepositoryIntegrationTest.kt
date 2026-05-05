package com.example.buy_it.repository

import android.util.Log
import com.example.buy_it.data.datasource.AuthRemoteDataSource
import com.example.buy_it.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.buy_it.data.dtos.UserProfileFirestoreDTO
import com.example.buy_it.data.repository.ReviewRepository
import com.example.buy_it.data.repository.UserRepository
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.buy_it.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.buy_it.data.datasource.impl.firestore.ProductFirestoreDataSourceImpl

class UserRepositoryIntegrationTest {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var userRepository: UserRepository

    private fun generateUser(i: Int): UserProfileFirestoreDTO = UserProfileFirestoreDTO(
        id = "user_$i",
        username = "username_$i",
        name = "Name $i",
        pfpURL = "https://example.com/profile_$i.jpg",
        biography = "Biography for user $i",
        created = "2026-05-05",
        email = "user_$i@example.com",
        password = "123456",
        followersCount = i * 10,
        followingCount = i * 2,
        followed = false
    )

    @Test
    fun updateUserProfile_validId_updatesName() = runTest {
        val result = userRepository.updateUserProfile(
            userId = "user_1",
            name = "Nuevo Nombre",
            pfpURL = null
        )

        val userResult = userRepository.getUserById("user_1")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(userResult.getOrNull()?.name).isEqualTo("Nuevo Nombre")
    }

    @Test
    fun updateUserProfile_validId_updatesPhoto() = runTest {
        val newPhoto = "https://example.com/new_photo.jpg"

        val result = userRepository.updateUserProfile(
            userId = "user_1",
            name = "Name 1",
            pfpURL = newPhoto
        )

        val userResult = userRepository.getUserById("user_1")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(userResult.getOrNull()?.pfpURL).isEqualTo(newPhoto)
    }

    @Test
    fun getUserReviews_validId_returnsEmptyList() = runTest {
        val result = userRepository.getUserReviews("user_1")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun followOrUnfollowUser_withoutAuth_returnsFailure() = runTest {
        val result = userRepository.followOrUnfollowUser("user_2")

        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message)
            .isEqualTo("No hay un usuario autenticado")
    }

    @Test
    fun getFollowingIds_withoutAuth_returnsFailure() = runTest {
        val result = userRepository.getFollowingIds()

        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message)
            .isEqualTo("No hay un usuario autenticado")
    }

    @Test
    fun getFollowers_userWithoutFollowers_returnsEmptyList() = runTest {
        val result = userRepository.getFollowers("user_1")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).isEmpty()
    }

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {

        }

        val authRemoteDataSource = AuthRemoteDataSource(auth)

        val productRemoteDataSource = ProductFirestoreDataSourceImpl(db)

        val reviewRepository = ReviewRepository(
            reviewRemoteDataSource = ReviewFirestoreDataSourceImpl(db),
            productRemoteDataSource = productRemoteDataSource,
            authRemoteDataSource = authRemoteDataSource
        )

        userRepository = UserRepository(
            userRemoteDatasource = UserFirestoreDataSourceImpl(db),
            reviewRepository = reviewRepository,
            authRemoteDataSource = authRemoteDataSource
        )

        val batch = db.batch()

        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }

        Log.d("TAG", "Antes del batch")
        batch.commit().await()
        Log.d("TAG", "Despues del batch")
    }

    @Test
    fun getUserById_validId_correctUser() = runTest {
        val id = "user_9"
        val expectedName = "Name 9"
        val result = userRepository.getUserById(id)

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result?.getOrNull()?.name).isEqualTo(expectedName)
    }

    @Test
    fun getUserById_invalidId_returnFailure() = runTest {
        val id = "user_999"
        val result = userRepository.getUserById(id)

        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result?.exceptionOrNull()?.message).isEqualTo("Usuario no encontrado")
    }

    @After
    fun tearDown() = runTest {
        val users = db.collection("users").get().await()

        for (userDoc in users) {
            val followers = userDoc.reference.collection("followers").get().await()
            for (follower in followers) {
                follower.reference.delete().await()
            }
            val following = userDoc.reference.collection("following").get().await()
            for (f in following) {
                f.reference.delete().await()
            }
        }

        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }
    }
}