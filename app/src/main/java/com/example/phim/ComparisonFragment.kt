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
    Fragment(
        R.layout.fragment_comparison
    ) {

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

        val movieAButton =
            view.findViewById<
                    Button
                    >(
                R.id.movieAButton
            )

        val movieBButton =
            view.findViewById<
                    Button
                    >(
                R.id.movieBButton
            )

        val statusText =
            view.findViewById<
                    TextView
                    >(
                R.id.comparisonStatus
            )

        val finishButton =
            view.findViewById<
                    Button
                    >(
                R.id.finishComparisonButton
            )

        movieAButton
            .setOnClickListener {

                val current =
                    viewModel
                        .currentMovie
                        .value

                val comparison =
                    viewModel
                        .comparisonMovie
                        .value

                if (
                    current != null &&
                    comparison != null
                ) {

                    viewModel
                        .chooseWinner(
                            current,
                            comparison
                        )
                }
            }

        movieBButton
            .setOnClickListener {

                val current =
                    viewModel
                        .currentMovie
                        .value

                val comparison =
                    viewModel
                        .comparisonMovie
                        .value

                if (
                    current != null &&
                    comparison != null
                ) {

                    viewModel
                        .chooseWinner(
                            comparison,
                            current
                        )
                }
            }

        finishButton
            .setOnClickListener {
                viewModel.finishComparisons()
                findNavController()
                    .navigate(R.id.action_comparisonFragment_to_profileFragment)
            }

        viewLifecycleOwner
            .lifecycleScope
            .launch {
                viewLifecycleOwner
                    .repeatOnLifecycle(
                        Lifecycle.State.STARTED
                    ) {
                        launch {
                            viewModel
                                .currentMovie
                                .collect {
                                    movieAButton.text =
                                        it?.movieName
                                            ?: "" }
                        }

                        launch {

                            viewModel
                                .comparisonMovie
                                .collect {

                                    if (
                                        it == null
                                    ) {

                                        movieAButton
                                            .isEnabled =
                                            false

                                        movieBButton
                                            .isEnabled =
                                            false

                                        finishButton
                                            .visibility =
                                            View.VISIBLE

                                        statusText.text =
                                            "Ranking complete"

                                    } else {

                                        movieAButton
                                            .isEnabled =
                                            true

                                        movieBButton
                                            .isEnabled =
                                            true

                                        finishButton
                                            .visibility =
                                            View.GONE

                                        statusText.text =
                                            "Which movie do you prefer?"

                                        movieBButton.text =
                                            it.movieName
                                    }
                                }
                        }
                    }
            }
    }
}