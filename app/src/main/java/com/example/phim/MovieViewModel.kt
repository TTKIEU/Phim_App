package com.example.phim

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieViewModel : ViewModel() {

    private val _rankings =
        MutableStateFlow<List<MoviePost>>(
            emptyList()
        )

    val rankings: StateFlow<List<MoviePost>> =
        _rankings.asStateFlow()


    fun addRanking(moviePost: MoviePost) {

        _rankings.value =
            (_rankings.value + moviePost)
                .sortedByDescending {
                    it.rating
                }
    }
}