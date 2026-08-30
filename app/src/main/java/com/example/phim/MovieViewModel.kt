package com.example.phim

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow
import kotlin.math.round

class MovieViewModel : ViewModel() {

    private val _rankings =
        MutableStateFlow<List<MoviePost>>(emptyList())

    val rankings: StateFlow<List<MoviePost>> =
        _rankings.asStateFlow()


    // Movie currently going through comparisons
    private val _currentMovie =
        MutableStateFlow<MoviePost?>(null)

    val currentMovie: StateFlow<MoviePost?> =
        _currentMovie.asStateFlow()


    // Movie the current movie is being compared against
    private val _comparisonMovie =
        MutableStateFlow<MoviePost?>(null)

    val comparisonMovie: StateFlow<MoviePost?> =
        _comparisonMovie.asStateFlow()


    private val alreadyComparedIds =
        mutableSetOf<String>()


    fun addRanking(
        username: String,
        movieName: String,
        genre: String,
        reviewLevel: ReviewLevel,
        notes: String
    ) {

        val startingRating =
            when (reviewLevel) {

                ReviewLevel.GREAT -> 8.5

                ReviewLevel.OK -> 6.5

                ReviewLevel.BAD -> 3.5
            }

        val movie =
            MoviePost(
                username = username,
                movieName = movieName,
                genre = genre,
                reviewLevel = reviewLevel,
                notes = notes,
                rating = startingRating
            )

        _rankings.value =
            _rankings.value + movie

        _currentMovie.value = movie
        alreadyComparedIds.clear()

        findNextComparison()
    }

    private fun findNextComparison() {

        val current =
            _currentMovie.value
                ?: return

        val candidates =
            _rankings.value
                .filter {
                    it.id != current.id
                }
                .filter {
                    it.id !in alreadyComparedIds
                }
                .filter {
                    // Prefer movies in the same category first
                    it.reviewLevel ==
                            current.reviewLevel
                }
                .sortedBy {
                    // Compare against movies close in rating
                    kotlin.math.abs(
                        it.rating - current.rating
                    )
                }

        _comparisonMovie.value =
            candidates.firstOrNull()
    }


    fun chooseWinner(
        winner: MoviePost,
        loser: MoviePost
    ) {

        updateRatings(
            winner = winner,
            loser = loser
        )

        val current =
            _currentMovie.value

        if (current != null) {

            alreadyComparedIds.add(
                if (current.id == winner.id)
                    loser.id
                else
                    winner.id
            )

            // Get updated version of the current movie
            _currentMovie.value =
                _rankings.value.find {
                    it.id == current.id
                }
        }

        findNextComparison()
    }


    private fun updateRatings(
        winner: MoviePost,
        loser: MoviePost
    ) {
        val kFactor = 0.5

        val expectedWinner =
            expectedScore(
                winner.rating,
                loser.rating
            )
        val expectedLoser =
            expectedScore(
                loser.rating,
                winner.rating
            )

        val newWinnerRating =
            roundRating(
                winner.rating +
                        kFactor * (1.0 - expectedWinner)
            )

        val newLoserRating =
            roundRating(
                loser.rating +
                        kFactor * (0.0 - expectedLoser)
            )
        _rankings.value =
            _rankings.value
                .map { movie ->

                    when (movie.id) {

                        winner.id ->
                            movie.copy(
                                rating = newWinnerRating
                            )

                        loser.id ->
                            movie.copy(
                                rating = newLoserRating
                            )

                        else -> movie
                    }
                }
                .sortedByDescending {
                    it.rating
                }
    }


    private fun expectedScore(
        ratingA: Double,
        ratingB: Double
    ): Double {

        return 1.0 /
                (
                        1.0 +
                                10.0.pow(
                                    (ratingB - ratingA) / 2.0
                                )
                        )
    }


    fun finishComparisons() {

        _currentMovie.value = null
        _comparisonMovie.value = null

        alreadyComparedIds.clear()
    }
    private fun roundRating(rating: Double): Double {
        return round(
            rating.coerceIn(0.0, 10.0) * 10
        ) / 10
    }
}