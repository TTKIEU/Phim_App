package com.example.phim

import androidx.lifecycle.ViewModel
import com.example.phim.network.TmdbMovie
import com.example.phim.repository.RatingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow
import kotlin.math.round

class MovieViewModel :
    ViewModel() {

    private val repository =
        RatingRepository()

    private var ratingListener:
            ListenerRegistration? = null

    private val _rankings =
        MutableStateFlow<
                List<MoviePost>
                >(
            emptyList()
        )

    val rankings:
            StateFlow<List<MoviePost>> =
        _rankings.asStateFlow()


    private val _selectedMovie =
        MutableStateFlow<
                TmdbMovie?
                >(
            null
        )

    val selectedMovie:
            StateFlow<TmdbMovie?> =
        _selectedMovie.asStateFlow()


    private val _currentMovie =
        MutableStateFlow<
                MoviePost?
                >(
            null
        )

    val currentMovie:
            StateFlow<MoviePost?> =
        _currentMovie.asStateFlow()


    private val _comparisonMovie =
        MutableStateFlow<
                MoviePost?
                >(
            null
        )

    val comparisonMovie:
            StateFlow<MoviePost?> =
        _comparisonMovie.asStateFlow()


    private val alreadyComparedIds =
        mutableSetOf<String>()


    fun loadRankings() {

        ratingListener?.remove()
        ratingListener = null

        _rankings.value = emptyList()

        ratingListener =
            repository
                .listenToCurrentUserRatings { ratings ->

                    if (_currentMovie.value == null) {
                        _rankings.value =
                            ratings.sortedByDescending {
                                it.rating
                            }
                    }
                }
    }


    fun selectMovie(
        movie: TmdbMovie
    ) {

        _selectedMovie.value =
            movie
    }


    fun addSelectedMovie(
        reviewLevel: ReviewLevel,
        notes: String
    ) {
        val selected =
            _selectedMovie.value
                ?: return

        val firebaseUser =
            FirebaseAuth
                .getInstance()
                .currentUser
                ?: return

        val username =
            firebaseUser
                .displayName
                ?: firebaseUser
                    .email
                    ?.substringBefore("@")
                ?: "User"

        val startingRating =
            when (
                reviewLevel
            ) {

                ReviewLevel.GREAT ->
                    8.5

                ReviewLevel.OK ->
                    6.5

                ReviewLevel.BAD ->
                    3.5
            }
        val now =
            System.currentTimeMillis()
        val movie =
            MoviePost(
                userId = firebaseUser.uid,
                username = username,
                tmdbId = selected.id,
                movieName = selected.title,
                posterPath = selected.posterPath,
                releaseDate = selected.releaseDate ?: "",
                genre = GenreMapper.fromIds(
                    selected.genreIds
                ),
                reviewLevel = reviewLevel.name,
                notes = notes.trim(),
                rating = startingRating,
                createdAt = now,
                updatedAt = now
            )

        _rankings.value =
            (_rankings.value + movie)
                .distinctBy {
                    it.id
                }
                .sortedByDescending {
                    it.rating
                }

        _currentMovie.value = movie

        alreadyComparedIds.clear()

        repository.saveRating(movie)

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
                    it.reviewLevel == current.reviewLevel
                }
                .sortedBy {
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
            winner,
            loser
        )

        val current =
            _currentMovie.value

        if (
            current != null
        ) {

            alreadyComparedIds.add(

                if (
                    current.id ==
                    winner.id
                ) {

                    loser.id

                } else {

                    winner.id
                }
            )

            _currentMovie.value =
                _rankings.value
                    .find {

                        it.id ==
                                current.id
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

        var newWinnerRating =
            winner.rating +
                    kFactor * (1.0 - expectedWinner)

        var newLoserRating =
            loser.rating +
                    kFactor * (0.0 - expectedLoser)


        /*
         * IMPORTANT:
         *
         * A direct comparison is stronger evidence
         * than the initial Great / OK / Bad category.
         *
         * If the winner still ends up below the loser,
         * force their ratings to reflect the user's
         * actual preference.
         */
        if (newWinnerRating <= newLoserRating) {

            val midpoint =
                (newWinnerRating + newLoserRating) / 2.0

            newWinnerRating =
                midpoint + 0.1

            newLoserRating =
                midpoint - 0.1
        }


        newWinnerRating =
            roundRating(
                newWinnerRating
                    .coerceIn(0.0, 10.0)
            )

        newLoserRating =
            roundRating(
                newLoserRating
                    .coerceIn(0.0, 10.0)
            )


        val now =
            System.currentTimeMillis()

        var updatedWinner: MoviePost? = null
        var updatedLoser: MoviePost? = null


        _rankings.value =
            _rankings.value
                .map { movie ->

                    when (movie.id) {

                        winner.id -> {

                            movie.copy(
                                rating = newWinnerRating,
                                updatedAt = now
                            ).also {

                                updatedWinner = it
                            }
                        }

                        loser.id -> {

                            movie.copy(
                                rating = newLoserRating,
                                updatedAt = now
                            ).also {

                                updatedLoser = it
                            }
                        }

                        else -> movie
                    }
                }
                .sortedByDescending {
                    it.rating
                }


        updatedWinner?.let {
            repository.saveRating(it)
        }

        updatedLoser?.let {
            repository.saveRating(it)
        }
    }


    private fun expectedScore(
        ratingA: Double,
        ratingB: Double
    ): Double {
        return 1.0 / (1.0 + 10.0.pow((ratingB - ratingA) / 2.0))
    }


    private fun roundRating(
        rating: Double
    ): Double {
        return round(
            rating
                .coerceIn(
                    0.0,
                    10.0
                ) *
                    10
        ) / 10
    }


    fun finishComparisons() {

        _currentMovie.value = null
        _comparisonMovie.value = null
        _selectedMovie.value = null

        alreadyComparedIds.clear()
    }


    override fun onCleared() {
        ratingListener
            ?.remove()
        super.onCleared()
    }
    fun clearUserData() {

        ratingListener?.remove()
        ratingListener = null

        _rankings.value = emptyList()

        _selectedMovie.value = null
        _currentMovie.value = null
        _comparisonMovie.value = null

        alreadyComparedIds.clear()
    }
}
