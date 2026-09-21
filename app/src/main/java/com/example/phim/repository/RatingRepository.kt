package com.example.phim.repository

import com.example.phim.MoviePost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class RatingRepository : RatingDataSource {

    private val auth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()

    //functions follow user lookup -> if exists, get collection of ratings and store
    //new rating
    override fun currentUserId(): String? = auth.currentUser?.uid

    override fun saveRating(
        movie: MoviePost,
        onComplete: (Boolean) -> Unit
    ) {
        val uid = currentUserId() ?: run { onComplete(false);return }
        val storedMovie =
            movie.copy(
                userId = uid,
                updatedAt =
                    System.currentTimeMillis()
            )
        db.collection("ratings")
            .document(storedMovie.id)
            .set(storedMovie)
            .addOnSuccessListener {

                onComplete(true)
            }
            .addOnFailureListener {

                onComplete(false)
            }
    }

    //set up fire store listener for the current logged-in user's ratings
    override fun listenToCurrentUserRatings(
        //on ratings changed == when I get the latest rankings, ill return that to the function calling me
        onRatingsChanged: (List<MoviePost>) -> Unit): ListenerRegistration? {

        val uid =
            auth.currentUser
                ?.uid
                ?: return null

        //return ratings collection where id of rating == user's
        return db.collection("ratings").whereEqualTo("userId", uid).addSnapshotListener { snapshot, error ->
                if (
                    //if there is an error, ignore this CURRENT snapshot
                    error != null ||
                    snapshot == null
                ) {
                    return@addSnapshotListener
                }

                //going through the list of non-null snapshots, convert them into movie post objects and sort
                val ratings = snapshot.documents.mapNotNull { it.toObject(MoviePost::class.java) }.sortedByDescending { it.rating }
                onRatingsChanged(ratings)
            }
    }

    override fun loadRecentRatingsForUsers(
        userIds: List<String>,
        onResult: (List<MoviePost>) -> Unit
    ) {

        if (userIds.isEmpty()) {
            onResult(emptyList())
            return
        }

        //load 30 most recent movies
        val chunks = userIds.chunked(30)

        val allRatings = mutableListOf<MoviePost>()

        var finishedQueries = 0

        // for every chunk of user ids (friends), search up their rankings
        //translate json to movie post object
        chunks.forEach { chunk ->
            db.collection("ratings")
                .whereIn(
                    "userId",
                    chunk
                ).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result
                            ?.documents
                            ?.mapNotNull {
                                it.toObject(
                                    MoviePost::class.java
                                )
                            }?.let { allRatings.addAll(it) }
                    }

                    finishedQueries++

                    if (
                        finishedQueries == chunks.size
                    ) {
                        //after finishing searching query amount of ids, sort their rated movies
                        //and return the recent 100 of them
                        onResult(
                            allRatings
                                .sortedByDescending {
                                    it.createdAt
                                }
                                .take(100)
                        )
                    }
                }
        }
    }
}