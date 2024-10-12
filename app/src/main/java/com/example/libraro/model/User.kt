package com.example.libraro.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.Timestamp

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val emailAddress: String = "",
    val profilePicture: String = "",
    val totalReadingTime: Long = 0,
    val booksCompleted: Int = 0,
    val purchasedBooks: List<String> = listOf(),
    val favoriteBooks: List<String> = listOf(),
    val wishlistBooks: List<String> = listOf(),
    @PropertyName("readingProgress")
    val readingProgress: Map<String, BookProgress> = mapOf(),
    val themePreference: String = "light",
    @PropertyName("lastLoginDate")
    val lastLoginDate: Timestamp? = null
)

data class BookProgress(
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    @PropertyName("lastReadDate")
    val lastReadDate: Timestamp? = null
)