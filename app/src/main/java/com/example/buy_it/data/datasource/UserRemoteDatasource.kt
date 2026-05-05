package com.example.buy_it.data.datasource

import com.example.buy_it.data.dtos.RegisterUserDto
import com.example.buy_it.data.dtos.ReviewDTO
import com.example.buy_it.data.dtos.UserDtoGeneric
import com.example.buy_it.data.dtos.UserDTO

interface UserRemoteDatasource {

    suspend fun getUserById(id: String): UserDtoGeneric?
    suspend fun getUserReviews(id: String): List<ReviewDTO>
    suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String)
    suspend fun updateUserProfile(userId: String, name: String, pfpURL: String?)
    suspend fun getUserById(id: String, currentUserId: String?): UserDtoGeneric?
    suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String)
    suspend fun getFollowingIds(userId: String): List<String>
    suspend fun getFollowerIds(userId: String): List<String>
    suspend fun getUsersByIds(userIds: List<String>): List<UserDTO>
}