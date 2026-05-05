package com.example.buy_it

import android.util.Log
import com.example.buy_it.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.buy_it.data.dtos.RegisterUserDto
import com.example.buy_it.data.dtos.UserProfileFirestoreDTO
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FirebaseUserDataSourceTest {

    private val db = Firebase.firestore
    private lateinit var dataSource: UserFirestoreDataSourceImpl
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

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {

        }

        dataSource = UserFirestoreDataSourceImpl(db)

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
        val result = dataSource.getUserById(id, "")

        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo(expectedName)
        Truth.assertThat(result?.id).isEqualTo(id)
    }

    @Test
    fun getUserById_invalidId_null() = runTest {
        val id = "user_999"
        val result = dataSource.getUserById(id, "")

        Truth.assertThat(result).isNull()
    }

    @Test
    fun registerUser_insertDocument_DocumentExists() = runTest {
        val user = RegisterUserDto(
            username = "username",
            name = "name",
            fcmToken = ""
        )

        dataSource.registerUser(user, "999")

        val result = dataSource.getUserById("999", "")

        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo("name")
        Truth.assertThat(result?.id).isEqualTo("999")
    }

    @Test
    fun followOrUnfollowUser_followUser_UserFollowed() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)

        Truth.assertThat(targetUserResult?.followed).isTrue()
    }

    @Test
    fun followOrUnfollowUser_followUser_followersCountIncrement() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        val oldData = dataSource.getUserById(targetUser.id)
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        val targetUserResult = dataSource.getUserById(targetUser.id)

        Truth.assertThat(targetUserResult?.followersCount).isGreaterThan(oldData?.followersCount)
    }

    @Test
    fun followOrUnfollowUser_unfollow_followedFalse() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)

        Truth.assertThat(targetUserResult?.followed).isFalse()
    }

    @Test
    fun getFollowingIds_userFollowing_returnsTargetUserId() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        val followingIds = dataSource.getFollowingIds(currentUser.id)

        Truth.assertThat(followingIds).contains(targetUser.id)
    }

    @Test
    fun getFollowerIds_userFollowed_returnsCurrentUserId() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        val followerIds = dataSource.getFollowerIds(targetUser.id)

        Truth.assertThat(followerIds).contains(currentUser.id)
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