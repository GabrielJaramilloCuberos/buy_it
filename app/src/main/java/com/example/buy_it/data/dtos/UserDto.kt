package com.example.buy_it.data.dtos

data class UserDTO(
    val id: String,
    val name: String,
    val pfpURL: String?
){
    constructor(): this("", "", "")
}