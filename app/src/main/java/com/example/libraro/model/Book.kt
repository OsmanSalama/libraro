package com.example.libraro.model

data class Book(
    val id: String,
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val coverImageUrl: String = "",
    val pdfUrl: String = "",
    val rating: Double = 0.0,
    val purchaseCount: Int = 0
)