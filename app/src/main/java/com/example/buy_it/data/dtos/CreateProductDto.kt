package com.example.buy_it.data.dtos

data class CreateProductDto(
    val name: String,
    val brand: String,
    val imageUrl: String?,
    val description: String?,
    val range: String?,
)