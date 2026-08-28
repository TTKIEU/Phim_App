package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class RankingFragment : Fragment(R.layout.fragment_ranking) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val movieName =
            view.findViewById<EditText>(R.id.movieNameInput)

        val rating =
            view.findViewById<EditText>(R.id.ratingInput)

        val submitButton =
            view.findViewById<Button>(R.id.submitRankingButton)

        submitButton.setOnClickListener {

            val movie = movieName.text.toString()
            val ratingValue =
                rating.text.toString().toDoubleOrNull()

            if (movie.isNotBlank() && ratingValue != null) {

                val newRanking = MoviePost(
                    username = "Taylin",
                    movieName = movie,
                    rating = ratingValue
                )

                // We'll connect this to persistent profile data next.
                findNavController().popBackStack()
            }
        }
    }
}