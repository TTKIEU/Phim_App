package com.example.phim

import java.util.UUID

data class MoviePost(

    val id: String =
        UUID.randomUUID().toString(),

    val userId: String = "",
    val username: String = "",
    val tmdbId: Int = 0,
    val movieName: String = "",
    val posterPath: String? = null,
    val releaseDate: String = "",
    val genre: String = "",
    val reviewLevel: String = "",
    val notes: String = "",
    val rating: Double = 0.0,
    val createdAt: Long =
        System.currentTimeMillis(),
    val updatedAt: Long =
        System.currentTimeMillis()
)

enum class ReviewLevel {
    GREAT,
    OK,
    BAD
}