package com.example.phim

import androidx.lifecycle.ViewModel
import com.example.phim.repository.FriendsRepository
import com.example.phim.repository.RatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedViewModel :
    ViewModel() {
    //want to view your friend's recently ranked
    private val friendsRepository = FriendsRepository()
    private val ratingRepository = RatingRepository()

    //create another state flow feed
    private val _feed = MutableStateFlow<List<MoviePost>>(emptyList())
    val feed: StateFlow<List<MoviePost>> = _feed.asStateFlow()


    //refresh feed
    fun refresh() {
        friendsRepository.loadFriends { friends ->
            //get id of friends
                val ids = friends.map { it.id }
            //load their recent ratings using their ids as the key
                ratingRepository.loadRecentRatingsForUsers(ids) { ratings ->
                        _feed.value = ratings
                    }
            }
    }
}