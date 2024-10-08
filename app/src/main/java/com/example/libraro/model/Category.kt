package com.example.libraro.model

data class Category(
    val name: String = "",
    val description: String = "",
    val bookIds: List<String> = listOf()
)
