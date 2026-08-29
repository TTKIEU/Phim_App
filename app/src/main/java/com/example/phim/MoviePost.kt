package com.example.phim

data class MoviePost(
    val username: String,
    val movieName: String,
    val genre: String,
    val reviewLevel: ReviewLevel,
    val notes: String,
    val rating: Double
)
enum class ReviewLevel {
    GREAT,
    OK,
    BAD
}