package com.example.phim.repository

import com.example.phim.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

//handles authentication using fireBase API calls
class AuthRepository {

    //get the instances of Fire base (authentication) and fire store (database)
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun currentUser() = auth.currentUser


    fun login(
        email: String,
        password: String,
        onResult: (
            success: Boolean,
            message: String?
        ) -> Unit
    ) {

        //basically just invoking firebase functions to login using the fire base auth object
        auth.signInWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener {
                onResult(
                    true,
                    null
                )
            }
            //logging for debugging
            .addOnFailureListener { exception ->
                android.util.Log.e(
                    "FirebaseAuth",
                    "createUserWithEmailAndPassword failed",
                    exception
                )

                onResult(
                    false,
                    "${exception.javaClass.simpleName}: ${exception.message}"
                )
            }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        onResult: (
            success: Boolean,
            message: String?
        ) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener {

                val firebaseUser =
                    auth.currentUser
                        ?: return@addOnSuccessListener

                val profileUpdate =
                    UserProfileChangeRequest
                        .Builder()
                        .setDisplayName(
                            username
                        )
                        .build()

                firebaseUser
                    .updateProfile(
                        profileUpdate
                    )

                val user =
                    User(
                        id =
                            firebaseUser.uid,

                        username =
                            username,

                        usernameLowercase =
                            username.lowercase(),

                        email =
                            email
                    )

                db.collection("users")
                    .document(
                        firebaseUser.uid
                    )
                    .set(user)
                    .addOnSuccessListener {

                        onResult(
                            true,
                            null
                        )
                    }
                    .addOnFailureListener {

                        onResult(
                            false,
                            it.message
                        )
                    }
            }
            .addOnFailureListener {

                onResult(
                    false,
                    it.message
                )
            }
    }

    fun logout() {
        auth.signOut()
    }
}