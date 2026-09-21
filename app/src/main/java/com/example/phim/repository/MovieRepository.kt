package com.example.phim.repository

import com.example.phim.network.TmdbClient
import com.example.phim.network.TmdbMovie

//TMDB Communicator
class MovieRepository {

    //indicate that this function is suspendable
    //if movie is taking too long to find/internet connection is spotty when coroutine is
    //run on this, suspend
    suspend fun searchMovies(
        query: String
    ): List<TmdbMovie> {
        //blank query handler
        if (
            query.isBlank()
        ) {
            return emptyList()
        }
        //pure alpha character query
        //return using api's given search movies function
        return TmdbClient.api.searchMovies(query.trim()).results
    }
}