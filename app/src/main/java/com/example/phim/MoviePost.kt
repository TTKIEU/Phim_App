package com.example.phim

import java.util.UUID

data class MoviePost(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val movieName: String,
    val genre: String,
    val reviewLevel: ReviewLevel,
    val notes: String,
    //calculated in the app
    val rating: Double
)

enum class ReviewLevel {
    GREAT,
    OK,
    BAD
}