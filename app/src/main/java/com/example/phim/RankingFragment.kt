package com.example.phim

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import android.widget.ImageView

class RankingFragment :
    Fragment(
        R.layout.fragment_ranking
    ) {

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

        val movie =
            viewModel
                .selectedMovie
                .value
                ?: run {

                    findNavController()
                        .popBackStack()

                    return
                }

        val poster =
            view.findViewById<
                    ImageView
                    >(
                R.id.rankingMoviePoster
            )

        val movieTitle =
            view.findViewById<
                    TextView
                    >(
                R.id.rankingMovieTitle
            )

        val releaseDate =
            view.findViewById<
                    TextView
                    >(
                R.id.rankingReleaseDate
            )

        val genre =
            view.findViewById<
                    TextView
                    >(
                R.id.rankingGenre
            )

        val notesInput =
            view.findViewById<
                    EditText
                    >(
                R.id.notesInput
            )

        val greatButton =
            view.findViewById<
                    MaterialButton
                    >(
                R.id.greatButton
            )

        val okButton =
            view.findViewById<
                    MaterialButton
                    >(
                R.id.okButton
            )

        val badButton =
            view.findViewById<
                    MaterialButton
                    >(
                R.id.badButton
            )

        val submitButton =
            view.findViewById<
                    MaterialButton
                    >(
                R.id.submitRankingButton
            )

        val backButton =
            view.findViewById<
                    ImageButton
                    >(
                R.id.backToProfile
            )

        movieTitle.text =
            movie.title

        releaseDate.text =
            movie.releaseDate
                ?.take(4)
                ?: ""

        genre.text =
            GenreMapper.fromIds(
                movie.genreIds
            )

        movie.posterPath
            ?.let {

                Glide.with(this)
                    .load(
                        "https://image.tmdb.org/t/p/w500$it"
                    )
                    .into(poster)
            }

        addClickAnimation(
            greatButton
        )

        addClickAnimation(
            okButton
        )

        addClickAnimation(
            badButton
        )

        greatButton
            .setOnClickListener {

                selectedReviewLevel =
                    ReviewLevel.GREAT

                updateSelectedButton(
                    greatButton,
                    greatButton,
                    okButton,
                    badButton
                )
            }

        okButton
            .setOnClickListener {

                selectedReviewLevel =
                    ReviewLevel.OK

                updateSelectedButton(
                    okButton,
                    greatButton,
                    okButton,
                    badButton
                )
            }

        badButton
            .setOnClickListener {

                selectedReviewLevel =
                    ReviewLevel.BAD

                updateSelectedButton(
                    badButton,
                    greatButton,
                    okButton,
                    badButton
                )
            }

        submitButton
            .setOnClickListener {

                val level =
                    selectedReviewLevel
                        ?: return@setOnClickListener

                viewModel
                    .addSelectedMovie(
                        level,
                        notesInput
                            .text
                            .toString()
                    )

                if (
                    viewModel
                        .comparisonMovie
                        .value != null
                ) {

                    findNavController()
                        .navigate(
                            R.id.action_rankingFragment_to_comparisonFragment
                        )

                } else {

                    viewModel
                        .finishComparisons()

                    findNavController()
                        .navigate(
                            R.id.action_rankingFragment_to_profileFragment
                        )
                }
            }

        backButton
            .setOnClickListener {

                findNavController()
                    .popBackStack()
            }
    }

    @SuppressLint(
        "ClickableViewAccessibility"
    )
    private fun addClickAnimation(
        button: View
    ) {

        button.setOnTouchListener {
                view,
                event ->

            when (
                event.action
            ) {

                MotionEvent.ACTION_DOWN -> {

                    view.animate()
                        .scaleX(0.92f)
                        .scaleY(0.92f)
                        .setDuration(80)
                        .start()
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {

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
        selected: MaterialButton,
        great: MaterialButton,
        ok: MaterialButton,
        bad: MaterialButton
    ) {

        great.alpha = 1f
        ok.alpha = 1f
        bad.alpha = 1f

        selected.alpha =
            0.7f
    }
}