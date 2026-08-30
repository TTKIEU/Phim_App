package com.example.phim

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import android.widget.ImageButton

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
            view.findViewById<com.google.android.material.button.MaterialButton>(
                R.id.greatButton
            )

        val okButton =
            view.findViewById<com.google.android.material.button.MaterialButton>(
                R.id.okButton
            )

        val badButton =
            view.findViewById<com.google.android.material.button.MaterialButton>(
                R.id.badButton
            )
        addClickAnimation(greatButton)
        addClickAnimation(okButton)
        addClickAnimation(badButton)
        addClickAnimation(movieNameInput)
        addClickAnimation(genreSpinner)
        addClickAnimation(notesInput)

        val submitButton =
            view.findViewById<Button>(
                R.id.submitRankingButton
            )
        addClickAnimation(submitButton)

        val backToProfile =
            view.findViewById<ImageButton>(
                R.id.backToProfile
            )

        backToProfile.setOnClickListener {
            findNavController().popBackStack()
        }

        greatButton.setOnClickListener {
            selectedReviewLevel = ReviewLevel.GREAT

            updateSelectedButton(
                greatButton,
                greatButton,
                okButton,
                badButton
            )
        }

        okButton.setOnClickListener {
            selectedReviewLevel = ReviewLevel.OK

            updateSelectedButton(
                okButton,
                greatButton,
                okButton,
                badButton
            )
        }

        badButton.setOnClickListener {
            selectedReviewLevel = ReviewLevel.BAD

            updateSelectedButton(
                badButton,
                greatButton,
                okButton,
                badButton
            )
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
    @SuppressLint("ClickableViewAccessibility")
    private fun addClickAnimation(button: View) {
        button.setOnTouchListener { view, event ->

            when (event.action) {

                android.view.MotionEvent.ACTION_DOWN -> {
                    view.animate()
                        .scaleX(0.92f)
                        .scaleY(0.92f)
                        .setDuration(80)
                        .start()
                }

                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL -> {
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(120)
                        .start()
                }
            }

            false
        }
    }
    private fun updateSelectedButton(
        selectedButton: com.google.android.material.button.MaterialButton,
        greatButton: com.google.android.material.button.MaterialButton,
        okButton: com.google.android.material.button.MaterialButton,
        badButton: com.google.android.material.button.MaterialButton
    ) {
        // Reset colors
        greatButton.backgroundTintList =
            android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#CC72D58A")
            )

        okButton.backgroundTintList =
            android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#CCD9CF65")
            )

        badButton.backgroundTintList =
            android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#CCC36D6D")
            )

        // Reset opacity
        greatButton.alpha = 1f
        okButton.alpha = 1f
        badButton.alpha = 1f

        // Grey/mute only the selected one
        selectedButton.alpha = 0.7f
    }
}