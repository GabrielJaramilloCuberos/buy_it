package com.example.buy_it.data.dtos

import com.example.buy_it.data.ReviewInfo
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class ReviewDTO(
    val id: String = "",
    val userId: String = "",
    val productId: String = "",
    val like: Boolean = false,
    val comment: String = "",
    val comments: Int = 0,
    val likesCount: Int = 0,
    var likedByCurrentUser: Boolean = false,
    val createdAt: Long = 0L,
    val user: UserDTO? = null,
    val product: ProductDTO? = null
) {
    constructor() : this("", "", "", false, "", 0, 0, false, 0L, null, null)
}

fun ReviewDTO.toReviewInfo(): ReviewInfo {
    val reviewDate = if (createdAt > 0L) {
        Instant.ofEpochMilli(createdAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } else {
        LocalDate.now()
    }

    return ReviewInfo(
        id = id,
        userId = userId,
        profileImage = user?.pfpURL ?: "",
        imgProd = product?.imageURL ?: "",
        name = user?.name ?: "Usuario desconocido",
        review = comment,
        productId = productId,
        product = product?.name ?: "",
        like = like,
        percentageLikes = product?.percentageLike ?: 0,
        date = reviewDate,
        range = product?.range ?: "",
        comments = comments,
        likesCount = likesCount,
        likedByCurrentUser = likedByCurrentUser
    )
}