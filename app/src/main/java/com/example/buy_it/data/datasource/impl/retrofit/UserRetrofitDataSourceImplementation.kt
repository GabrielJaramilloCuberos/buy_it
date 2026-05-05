package com.example.buy_it.data.datasource.impl.retrofit

import com.example.buy_it.data.datasource.UserRemoteDatasource
import com.example.buy_it.data.datasource.services.UserRetrofitService
import com.example.buy_it.data.dtos.RegisterUserDto
import com.example.buy_it.data.dtos.ReviewDTO
import com.example.buy_it.data.dtos.UserProfileRetrofitDTO
import com.example.buy_it.data.repository.AuthRepository
import javax.inject.Inject
import com.example.buy_it.data.dtos.UserDTO

class UserRetrofitDataSourceImplementation @Inject constructor(
    private val service: UserRetrofitService,
    private val repo : AuthRepository
) : UserRemoteDatasource {

    override suspend fun getUserById(id: String): UserProfileRetrofitDTO {
        return service.getUserById(id.toInt())
    }

    override suspend fun getUserReviews(id: String): List<ReviewDTO> {
        return service.getReviewsByUser(id)
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        val  email = repo.currentUser?.email ?: throw Exception("No se pudo obtener el email del usuario")
        val user = UserProfileRetrofitDTO(
            id = userId,
            username = registerUserDto.username,
            name = registerUserDto.name,
            pfpURL = null,
            biography = "",
            created = null,
            email = email,
            password = "",
            followersCount = 0,
            followingCount = 0,
            followed = false
        )

        return service.createUser(user)
    }

    override suspend fun updateUserProfile(userId: String, name: String, pfpURL: String?) {
    }

    override suspend fun getUserById(
        id: String,
        currentUserId: String?
    ): UserProfileRetrofitDTO {
        return getUserById(id)
    }

    override suspend fun followOrUnfollowUser(
        currentUserId: String,
        targetUserId: String
    ) {
    }

    override suspend fun getFollowingIds(userId: String): List<String> {
        return emptyList()
    }

    override suspend fun getFollowerIds(userId: String): List<String> {
        return emptyList()
    }

    override suspend fun getUsersByIds(userIds: List<String>): List<UserDTO> {
        return emptyList()
    }
}