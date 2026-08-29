package com.example.phim

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieViewModel : ViewModel(){
    private val _rankings =  MutableStateFlow(
            listOf(
            MoviePost(
            username = "Taylin",
            movieName = "Spider-Man: No Way Home",
            rating = 9.8
        ),

            MoviePost(
                username = "Nicole",
                movieName = "Interstellar",
                rating = 9.5
            ),

            MoviePost(
                username = "Chad",
                movieName = "The Batman",
                rating = 8.7
            )
        ))
    val rankings: StateFlow<List<MoviePost>> =
        _rankings.asStateFlow()
    fun addRanking(moviePost: MoviePost){
        _rankings.value = (
                _rankings.value + moviePost).sortedByDescending { it.rating }
    }

}