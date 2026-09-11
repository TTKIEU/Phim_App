package com.example.phim.repository

import com.example.phim.MoviePost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class RatingRepository {

    private val auth =
        FirebaseAuth.getInstance()

    private val db =
        FirebaseFirestore.getInstance()

    fun currentUserId(): String? =
        auth.currentUser?.uid

    fun saveRating(
        movie: MoviePost,
        onComplete: (
            Boolean
        ) -> Unit = {}
    ) {

        val uid =
            currentUserId()
                ?: run {

                    onComplete(false)

                    return
                }

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

    fun listenToCurrentUserRatings(
        onRatingsChanged: (List<MoviePost>) -> Unit
    ): ListenerRegistration? {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser
                ?.uid
                ?: return null

        return FirebaseFirestore
            .getInstance()
            .collection("ratings")
            .whereEqualTo(
                "userId",
                uid
            )
            .addSnapshotListener { snapshot, error ->

                if (
                    error != null ||
                    snapshot == null
                ) {
                    return@addSnapshotListener
                }

                val ratings =
                    snapshot.documents
                        .mapNotNull {
                            it.toObject(
                                MoviePost::class.java
                            )
                        }
                        .sortedByDescending {
                            it.rating
                        }

                onRatingsChanged(ratings)
            }
    }

    fun loadRecentRatingsForUsers(
        userIds: List<String>,
        onResult:
            (List<MoviePost>) -> Unit
    ) {

        if (userIds.isEmpty()) {

            onResult(emptyList())

            return
        }

        /*
         * Firestore limits whereIn values.
         * Chunking also lets this continue
         * working as the friend list grows.
         */
        val chunks =
            userIds.chunked(30)

        val allRatings =
            mutableListOf<MoviePost>()

        var finishedQueries = 0

        chunks.forEach { chunk ->

            db.collection("ratings")
                .whereIn(
                    "userId",
                    chunk
                )
                .get()
                .addOnCompleteListener {
                        task ->

                    if (task.isSuccessful) {

                        task.result
                            ?.documents
                            ?.mapNotNull {

                                it.toObject(
                                    MoviePost::class.java
                                )
                            }
                            ?.let {

                                allRatings.addAll(
                                    it
                                )
                            }
                    }

                    finishedQueries++

                    if (
                        finishedQueries ==
                        chunks.size
                    ) {

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