package com.example.phim.network

import retrofit2.http.GET
import retrofit2.http.Query

//REST API implementation
interface TmdbApi {

    @GET("search/movie")
    suspend fun searchMovies(

        @Query("query")
        query: String,

        @Query("include_adult")
        includeAdult: Boolean = false,

        @Query("language")
        language: String = "en-US"

    ): MovieSearchResponse
}