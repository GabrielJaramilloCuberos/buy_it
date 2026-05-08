package com.example.buy_it.data.dtos

data class PriceDTO(
    val id: String = "",
    val storeName: String = "",
    val price: Double = 0.0,
    val productId: String = "",
    val storeLogo: String = "",
    val percentage: Int = 0
)
