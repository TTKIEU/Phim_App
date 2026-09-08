package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MovieSearchFragment :
    Fragment(
        R.layout.fragment_movie_search
    ) {

    private val searchViewModel:
            SearchViewModel by viewModels()

    private val movieViewModel:
            MovieViewModel by activityViewModels()

    private lateinit var adapter:
            MovieSearchAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        val searchInput =
            view.findViewById<
                    EditText
                    >(
                R.id.movieSearchInput
            )

        val searchButton =
            view.findViewById<
                    Button
                    >(
                R.id.movieSearchButton
            )

        val backButton =
            view.findViewById<
                    ImageButton
                    >(
                R.id.searchBackButton
            )

        val progress =
            view.findViewById<
                    ProgressBar
                    >(
                R.id.searchProgress
            )

        val recyclerView =
            view.findViewById<
                    RecyclerView
                    >(
                R.id.searchRecyclerView
            )

        adapter =
            MovieSearchAdapter {
                    movie ->

                movieViewModel
                    .selectMovie(
                        movie
                    )

                findNavController()
                    .navigate(
                        R.id.action_movieSearchFragment_to_rankingFragment
                    )
            }

        recyclerView.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        recyclerView.adapter =
            adapter

        searchButton
            .setOnClickListener {

                searchViewModel.search(
                    searchInput
                        .text
                        .toString()
                )
            }

        backButton
            .setOnClickListener {

                findNavController()
                    .popBackStack()
            }

        viewLifecycleOwner
            .lifecycleScope
            .launch {

                viewLifecycleOwner
                    .repeatOnLifecycle(
                        Lifecycle.State.STARTED
                    ) {

                        launch {

                            searchViewModel
                                .movies
                                .collect {

                                    adapter.submitList(
                                        it
                                    )
                                }
                        }

                        launch {

                            searchViewModel
                                .loading
                                .collect {

                                    progress.visibility =
                                        if (it) {

                                            View.VISIBLE

                                        } else {

                                            View.GONE
                                        }
                                }
                        }
                    }
            }
    }
}