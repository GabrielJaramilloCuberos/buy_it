package com.example.buy_it

import android.util.Log
import com.example.buy_it.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.buy_it.data.dtos.UserProfileFirestoreDTO
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FirebaseUserDataSourceTest {

    private val db = Firebase.firestore
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
    fun getUserById_validId_correctUser() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (_: IllegalStateException) {}

        val dataSource = UserFirestoreDataSourceImpl(db)

        val batch = db.batch()

        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }

        batch.commit().await()

        val id = "user_9"
        val expectedName = "Name 9"
        val result = dataSource.getUserById(id, "")

        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo(expectedName)
        Truth.assertThat(result?.id).isEqualTo(id)

        val users = db.collection("users").get().await()
        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }
    }

    @Test
    fun getUserById_invalidId_null() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (_: IllegalStateException) {}

        val dataSource = UserFirestoreDataSourceImpl(db)

        val batch = db.batch()

        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }

        batch.commit().await()

        val id = "user_999"
        val result = dataSource.getUserById(id, "")

        Truth.assertThat(result).isNull()

        val users = db.collection("users").get().await()
        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }
    }
}