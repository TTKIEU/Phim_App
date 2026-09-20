package com.example.phim.repository

import com.example.phim.MoviePost;
import com.google.firebase.firestore.ListenerRegistration;

interface RatingDataSource{
    fun currentUserId(): String?
    fun saveRating(
        movie: MoviePost,
        onComplete: (Boolean) -> Unit = {}
    )

    fun listenToCurrentUserRatings(
        onRatingsChanged: (List<MoviePost>) -> Unit
    ): ListenerRegistration?

    fun loadRecentRatingsForUsers(
        userIds: List<String>,
        onResult: (List<MoviePost>) -> Unit
    )
}