package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class RankingFragment : Fragment(R.layout.fragment_ranking) {
    private val viewModel: MovieViewModel by activityViewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        //gathering the inputted data
        val movieNameInput =
            view.findViewById<EditText>(R.id.movieNameInput)

        val ratingInput =
            view.findViewById<EditText>(R.id.ratingInput)

        val submitButton =
            view.findViewById<Button>(R.id.submitRankingButton)
        //once submitted, compose it into a MoviePost object and hand over to the ViewModel
        submitButton.setOnClickListener {

            val movieName = movieNameInput.text.toString()
            val rating =
                ratingInput.text.toString().toDoubleOrNull()

            if (movieName.isNotBlank() && rating != null && rating in 0.0..10.0) {

                val newRanking = MoviePost(
                    username = "Taylin",
                    movieName = movieName,
                    rating = rating
                )
                viewModel.addRanking(newRanking)

                // We'll connect this to persistent profile data next.
                findNavController().popBackStack()
            }
        }
    }
}