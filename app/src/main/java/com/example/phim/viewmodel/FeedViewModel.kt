package com.example.phim

import androidx.lifecycle.ViewModel
import com.example.phim.repository.FriendsRepository
import com.example.phim.repository.RatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedViewModel :
    ViewModel() {

    private val friendsRepository =
        FriendsRepository()

    private val ratingRepository =
        RatingRepository()

    private val _feed =
        MutableStateFlow<
                List<MoviePost>
                >(
            emptyList()
        )

    val feed:
            StateFlow<List<MoviePost>> =
        _feed.asStateFlow()

    fun refresh() {

        friendsRepository
            .loadFriends {
                    friends ->

                val ids =
                    friends.map {
                        it.id
                    }

                ratingRepository
                    .loadRecentRatingsForUsers(
                        ids
                    ) {
                            ratings ->

                        _feed.value =
                            ratings
                    }
            }
    }
}