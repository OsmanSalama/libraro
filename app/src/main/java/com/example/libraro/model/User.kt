package com.example.libraro.model

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val profilePicture: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val totalReadingTime: String = "",
    val booksCompleted: String = "",
    val purchasedBooks: String = "",
    val favoriteBooks: String = "",
    val wishlistBooks: String = "",
    val readingProgress: String = "",
    val themePreference: String = ""
)