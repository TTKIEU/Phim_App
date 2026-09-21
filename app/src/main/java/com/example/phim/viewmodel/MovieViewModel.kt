package com.example.phim

import androidx.lifecycle.ViewModel
import com.example.phim.network.TmdbMovie
import com.example.phim.repository.RatingDataSource
import com.example.phim.repository.RatingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow
import kotlin.math.round
//Dependency Injection ready; RatingRepository() is a default value if nothing is passed
class MovieViewModel(
    private val repository: RatingDataSource = RatingRepository()
) :ViewModel() {

    //for firebase;
    //Allows for the synchronization of data to firebase
    //if not removed, will continuously listen to data (memory waste and firebase quota)
    private var ratingListener: ListenerRegistration? = null

    //Initialize rankings list with stateFlow
    private val _rankings = MutableStateFlow<List<MoviePost>>(emptyList())
    //READ-ONLY
    val rankings: StateFlow<List<MoviePost>> = _rankings.asStateFlow()

    // Current selected movie; can be null due to no selection
    private val _selectedMovie = MutableStateFlow<TmdbMovie?>(null)
    val selectedMovie: StateFlow<TmdbMovie?> = _selectedMovie.asStateFlow()

    //After the selection and initial ranking phase
    private val _currentMovie = MutableStateFlow<MoviePost?>(null)

    val currentMovie: StateFlow<MoviePost?> = _currentMovie.asStateFlow()

    // pairwise comparison movie; can be null due to no other movies left or in the list
    private val _comparisonMovie = MutableStateFlow<MoviePost?>(null)

    val comparisonMovie: StateFlow<MoviePost?> = _comparisonMovie.asStateFlow()

    // compared movies
    private val alreadyComparedIds = mutableSetOf<String>()


    fun loadRankings() {

        //firebase
        //if an old listener exists, have listener stop and remove reference
        //in case of loadRankings is called twice
        //makes sure to use a fresh listener
        ratingListener?.remove()
        ratingListener = null

        //start with an empty list/initializes _rankings; also helpful for cleaning up remaining data
        _rankings.value = emptyList()

        //create a new listener
        //start listening to current user's list and for any firebase updates
        //ONLY if I am not currently ranking something
        ratingListener = repository.listenToCurrentUserRatings { ratings ->
            if (_currentMovie.value == null) {
                _rankings.value =
                    ratings.sortedByDescending {
                        it.rating
                    }
            }
        }
    }

    //simple setter that sets current movie used later with user interaction in fragment
    fun selectMovie(movie: TmdbMovie) {
        _selectedMovie.value = movie
    }


    fun addSelectedMovie(reviewLevel: ReviewLevel, notes: String) {
        //Only if there is a selected movie else return
        val selected = _selectedMovie.value ?: return
        //get the current user in firebase
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
        //gather the metadata from the firebaseUID
        val username = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "User"

        //initializes the values of each reviewLevel
        val startingRating = when (reviewLevel) {
            ReviewLevel.GREAT ->
                8.5

            ReviewLevel.OK ->
                6.5

            ReviewLevel.BAD ->
                3.5
        }

        //convert current time (for time based sorting)
        val now = System.currentTimeMillis()

        //initialize the moviePost with user metadata from firebase
        //and current selected movie + selected ranking in ranking_01
        val movie = MoviePost(
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

        //add movie to the current rankings list
        _rankings.value = (_rankings.value + movie).distinctBy { it.id }.sortedByDescending { it.rating }
        //set current movie being pairwise ranked s.t. it moves to the next stage
        _currentMovie.value = movie
        //clear list of movies ranked against
        alreadyComparedIds.clear()
        //turn over to repository to save in firestore
        repository.saveRating(movie)
        //initialize pairwise ranking
        findNextComparison()
    }


    private fun findNextComparison() {
        //if there is no current movie being rated return
        val current = _currentMovie.value ?: return

        //filter the movies ot make sure I'm not ranking the same movie against itself
        // and if it hasn't already been ranked
        // and only if the review LEVEL is matching with the current movie
        // and sort ascending based on the difference value of the current ranking
        val candidates = _rankings.value.filter { it.id != current.id }.filter {it.id !in alreadyComparedIds }.filter { it.reviewLevel == current.reviewLevel }
            .sortedBy {
                kotlin.math.abs(
                    it.rating - current.rating
                )
            }
        //found the next ranking (the one closest in value with the current movie ranking)
        _comparisonMovie.value = candidates.firstOrNull()
    }


    fun chooseWinner(
        winner: MoviePost,
        loser: MoviePost
    ) {
        updateRatings(winner, loser)

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


    private fun updateRatings(winner: MoviePost, loser: MoviePost) {
        //Controls how much one comparison is allowed to change a movie's rating
        val kFactor = 0.5

        //predict the winner and loser based on current rankings
        val expectedWinner = expectedScore(winner.rating, loser.rating)
        val expectedLoser = expectedScore(loser.rating, winner.rating)

        //new rating = current rating + k *(actual result - expected result)
        //this is because, if a movie is expected to win, don't adjust it's score by that much
        //but if the underdog wins, make a larger adjustment
        var newWinnerRating = winner.rating + kFactor * (1.0 - expectedWinner)
        var newLoserRating = loser.rating + kFactor * (0.0 - expectedLoser)


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
        //guarantees that the winner is still above the loser
        //adjust their scores based on the midpoint even if loser has a greater ranking now
        if (newWinnerRating <= newLoserRating) {
            val midpoint = (newWinnerRating + newLoserRating) / 2.0

            newWinnerRating = midpoint + 0.1

            newLoserRating = midpoint - 0.1
        }

        //force rating format
        newWinnerRating = roundRating(newWinnerRating.coerceIn(0.0, 10.0))
        newLoserRating = roundRating(newLoserRating.coerceIn(0.0, 10.0))


        val now = System.currentTimeMillis()

        //clear any past data left over
        //you're making new objects later
        //save to access in firebase
        var updatedWinner: MoviePost? = null
        var updatedLoser: MoviePost? = null


        //new rankings is a map that maps a movie from rankings map
        // using the movie id as the key, it will make a copy of that object with the new updated ranking
        // and set it; do for both winner and loser
        //else move onto the next movie
        _rankings.value = _rankings.value.map { movie ->
            when (movie.id) {winner.id -> { movie.copy(rating = newWinnerRating, updatedAt = now).also { updatedWinner = it } }
                loser.id -> { movie.copy(rating = newLoserRating, updatedAt = now).also { updatedLoser = it } }
                else -> movie }
        }
            .sortedByDescending {
                it.rating
            }

        //call repository and update ranking
        updatedWinner?.let { repository.saveRating(it)}
        updatedLoser?.let { repository.saveRating(it)}
    }


    //how likely is A to win over b
    // i.e. 0.5 = 50/50
    private fun expectedScore(
        ratingA: Double,
        ratingB: Double
    ): Double {
        return 1.0 / (1.0 + 10.0.pow((ratingB - ratingA) / 2.0))
    }


    //standardize the ranking
    private fun roundRating(
        rating: Double
    ): Double {
        return round(rating.coerceIn(0.0, 10.0) * 10) / 10
    }


    //make sure to set all the references to null to be safe
    fun finishComparisons() {

        _currentMovie.value = null
        _comparisonMovie.value = null
        _selectedMovie.value = null

        alreadyComparedIds.clear()
    }


    override fun onCleared() {
        ratingListener?.remove()
        super.onCleared()
    }
    //for user changes
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
