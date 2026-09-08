package com.example.phim.model

data class Movie(
    val tmdbId: kotlin.Int = 0,
    val title: kotlin.String = "",
    val posterPath: kotlin.String? = null,
    val releaseDate: kotlin.String = "",
    val genreIds: kotlin.collections.List<kotlin.Int> = _root_ide_package_.kotlin.collections.emptyList()
)