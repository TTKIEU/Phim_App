package com.example.phim.network

import com.google.gson.annotations.SerializedName

data class TmdbMovie(

    val id: Int,

    val title: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("genre_ids")
    val genreIds: List<Int> = emptyList()
)