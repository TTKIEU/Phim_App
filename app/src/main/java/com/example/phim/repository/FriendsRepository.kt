package com.example.phim.repository

import com.example.phim.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FriendsRepository {

    private val auth =
        FirebaseAuth.getInstance()

    private val db =
        FirebaseFirestore.getInstance()

    fun searchUsers(
        query: String,
        onResult:
            (List<User>) -> Unit
    ) {

        val normalized =
            query.trim()
                .lowercase()

        if (normalized.isBlank()) {

            onResult(emptyList())

            return
        }

        db.collection("users")
            .whereGreaterThanOrEqualTo(
                "usernameLowercase",
                normalized
            )
            .whereLessThanOrEqualTo(
                "usernameLowercase",
                normalized + "\uf8ff"
            )
            .limit(20)
            .get()
            .addOnSuccessListener {
                    snapshot ->

                val currentUserId =
                    auth.currentUser?.uid

                val users =
                    snapshot.documents
                        .mapNotNull {

                            it.toObject(
                                User::class.java
                            )
                        }
                        .filter {

                            it.id !=
                                    currentUserId
                        }

                onResult(users)
            }
            .addOnFailureListener {

                onResult(
                    emptyList()
                )
            }
    }

    fun addFriend(
        friend: User,
        onResult:
            (Boolean) -> Unit
    ) {

        val uid =
            auth.currentUser?.uid
                ?: run {

                    onResult(false)

                    return
                }

        db.collection("users")
            .document(uid)
            .collection("friends")
            .document(friend.id)
            .set(friend)
            .addOnSuccessListener {

                onResult(true)
            }
            .addOnFailureListener {

                onResult(false)
            }
    }

    fun removeFriend(
        friendId: String,
        onResult:
            (Boolean) -> Unit
    ) {

        val uid =
            auth.currentUser?.uid
                ?: run {

                    onResult(false)

                    return
                }

        db.collection("users")
            .document(uid)
            .collection("friends")
            .document(friendId)
            .delete()
            .addOnSuccessListener {

                onResult(true)
            }
            .addOnFailureListener {

                onResult(false)
            }
    }

    fun loadFriends(
        onResult:
            (List<User>) -> Unit
    ) {

        val uid =
            auth.currentUser?.uid
                ?: run {

                    onResult(emptyList())

                    return
                }

        db.collection("users")
            .document(uid)
            .collection("friends")
            .get()
            .addOnSuccessListener {
                    snapshot ->

                onResult(
                    snapshot.documents
                        .mapNotNull {

                            it.toObject(
                                User::class.java
                            )
                        }
                )
            }
            .addOnFailureListener {

                onResult(
                    emptyList()
                )
            }
    }
}