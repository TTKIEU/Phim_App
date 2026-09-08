package com.example.phim.repository

import com.example.phim.network.TmdbClient
import com.example.phim.network.TmdbMovie

class MovieRepository {

    suspend fun searchMovies(
        query: String
    ): List<TmdbMovie> {

        if (
            query.isBlank()
        ) {
            return emptyList()
        }

        return TmdbClient
            .api
            .searchMovies(
                query.trim()
            )
            .results
    }
}