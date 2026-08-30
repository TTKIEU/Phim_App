package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class RankingFragment :
    Fragment(R.layout.fragment_ranking) {

    private val viewModel:
            MovieViewModel by activityViewModels()

    private var selectedReviewLevel:
            ReviewLevel? = null


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )


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


        greatButton.setOnClickListener {

            selectedReviewLevel =
                ReviewLevel.GREAT
        }


        okButton.setOnClickListener {

            selectedReviewLevel =
                ReviewLevel.OK
        }


        badButton.setOnClickListener {

            selectedReviewLevel =
                ReviewLevel.BAD
        }


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


            val reviewLevel =
                selectedReviewLevel


            if (
                movieName.isNotBlank() &&
                genre != "Genre" &&
                reviewLevel != null
            ) {

                viewModel.addRanking(
                    username = "Taylin",
                    movieName = movieName,
                    genre = genre,
                    reviewLevel = reviewLevel,
                    notes = notes
                )


                /*
                 * If there is another movie
                 * available for comparison,
                 * go to ComparisonFragment.
                 */

                if (
                    viewModel.comparisonMovie.value
                    != null
                ) {

                    findNavController()
                        .navigate(
                            R.id.action_rankingFragment_to_comparisonFragment
                        )

                } else {

                    /*
                     * First movie ever ranked.
                     * Nothing to compare against.
                     */

                    viewModel.finishComparisons()

                    findNavController()
                        .popBackStack()
                }
            }
        }
    }
}