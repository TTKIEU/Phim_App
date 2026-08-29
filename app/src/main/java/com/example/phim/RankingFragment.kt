package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class RankingFragment : Fragment(R.layout.fragment_ranking) {

    private val viewModel: MovieViewModel by activityViewModels()

    private var selectedRating = 0.0
    private var selectedReviewLevel = ""

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val movieNameInput =
            view.findViewById<EditText>(
                R.id.movieNameInput
            )

        val notesInput =
            view.findViewById<EditText>(
                R.id.notesInput
            )

        val genreSpinner =
            view.findViewById<Spinner>(
                R.id.genreSpinner
            )

        val greatButton =
            view.findViewById<Button>(
                R.id.greatButton
            )

        val okButton =
            view.findViewById<Button>(
                R.id.okButton
            )

        val badButton =
            view.findViewById<Button>(
                R.id.badButton
            )

        val submitButton =
            view.findViewById<Button>(
                R.id.submitRankingButton
            )


        // GREAT

        greatButton.setOnClickListener {

            selectedRating = 10.0
            selectedReviewLevel = "Great"
        }


        // OK

        okButton.setOnClickListener {

            selectedRating = 7.0
            selectedReviewLevel = "Ok"
        }


        // BAD

        badButton.setOnClickListener {

            selectedRating = 3.0
            selectedReviewLevel = "Bad"
        }


        // SUBMIT

        submitButton.setOnClickListener {

            val movieName =
                movieNameInput.text
                    .toString()
                    .trim()

            val notes =
                notesInput.text
                    .toString()
                    .trim()

            val genre =
                genreSpinner
                    .selectedItem
                    .toString()


            if (
                movieName.isNotBlank() &&
                selectedReviewLevel.isNotBlank() &&
                genre != "Genre"
            ) {

                val moviePost = MoviePost(

                    username = "Taylin",

                    movieName = movieName,

                    rating = selectedRating,

                    genre = genre,

                    reviewLevel = selectedReviewLevel,

                    notes = notes
                )

                viewModel.addRanking(moviePost)

                findNavController().popBackStack()
            }
        }
    }
}