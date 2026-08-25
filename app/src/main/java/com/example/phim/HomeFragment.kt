package com.example.phim

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<RecyclerView>(
                R.id.movieRecyclerView
            )

        val posts = listOf(

            MoviePost(
                username = "_Username_",
                movieName = "Spider-Man: No Way Home",
                rating = 9.8
            ),

            MoviePost(
                username = "Taylin",
                movieName = "Interstellar",
                rating = 9.5
            ),

            MoviePost(
                username = "Alex",
                movieName = "The Batman",
                rating = 8.7
            ),

            MoviePost(
                username = "Sam",
                movieName = "Dune: Part Two",
                rating = 9.2
            ),

            MoviePost(
                username = "Jordan",
                movieName = "Everything Everywhere All at Once",
                rating = 9.6
            )

        )

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerView.adapter =
            MovieAdapter(posts)
    }
}