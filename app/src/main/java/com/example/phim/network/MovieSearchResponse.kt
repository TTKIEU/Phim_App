package com.example.phim.network

//standardize TMDB json response
data class MovieSearchResponse(
    //use JSON translation found in TmdbMovie
    val results: List<TmdbMovie>
)