package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

class ComparisonFragment :
    Fragment(R.layout.fragment_comparison) {

    private val viewModel:
            MovieViewModel by activityViewModels()


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )


        val movieAName =
            view.findViewById<TextView>(
                R.id.movieAName
            )

        val movieBName =
            view.findViewById<TextView>(
                R.id.movieBName
            )

        val movieAButton =
            view.findViewById<Button>(
                R.id.movieAButton
            )

        val movieBButton =
            view.findViewById<Button>(
                R.id.movieBButton
            )

        val finishButton =
            view.findViewById<Button>(
                R.id.finishComparisonButton
            )


        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.currentMovie.collect {
                        current ->

                    current ?: return@collect

                    movieAName.text =
                        current.movieName

                    movieAButton.text =
                        current.movieName
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.comparisonMovie.collect {
                        comparison ->

                    if (comparison == null) {

                        finishButton.visibility =
                            View.VISIBLE

                        movieBButton.isEnabled =
                            false

                        movieBName.text =
                            "Finished!"

                    } else {

                        finishButton.visibility =
                            View.GONE

                        movieBButton.isEnabled =
                            true

                        movieBName.text =
                            comparison.movieName

                        movieBButton.text =
                            comparison.movieName
                    }
                }
            }
        }


        movieAButton.setOnClickListener {

            val current =
                viewModel.currentMovie.value

            val comparison =
                viewModel.comparisonMovie.value


            if (
                current != null &&
                comparison != null
            ) {

                viewModel.chooseWinner(
                    winner = current,
                    loser = comparison
                )
            }
        }


        movieBButton.setOnClickListener {

            val current =
                viewModel.currentMovie.value

            val comparison =
                viewModel.comparisonMovie.value


            if (
                current != null &&
                comparison != null
            ) {

                viewModel.chooseWinner(
                    winner = comparison,
                    loser = current
                )
            }
        }


        finishButton.setOnClickListener {

            viewModel.finishComparisons()

            findNavController()
                .navigate(
                    R.id.action_comparisonFragment_to_profileFragment
                )
        }
    }
}