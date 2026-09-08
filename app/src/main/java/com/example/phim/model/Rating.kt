package com.example.phim.model

data class Rating(
    val id: String = "",
    val userId: String = "",
    val username: String = "",

    val tmdbId: Int = 0,
    val movieTitle: String = "",
    val posterPath: String? = null,

    val rating: Double = 0.0,
    val reviewLevel: String = "",
    val notes: String = "",

    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)