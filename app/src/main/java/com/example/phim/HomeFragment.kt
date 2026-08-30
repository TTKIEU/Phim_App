package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        val profileButton =
            view.findViewById<ImageButton>(
                R.id.navProfileButton
            )

        val posts = listOf(

            MoviePost(
                username = "Taylin",
                movieName = "Interstellar",
                genre = "Sci-Fi",
                reviewLevel = ReviewLevel.GREAT,
                notes = "Amazing visuals and story.",
                rating = 8.0
            ),

            MoviePost(
                username = "Alex",
                movieName = "The Batman",
                genre = "Action",
                reviewLevel = ReviewLevel.GREAT,
                notes = "Loved the atmosphere and cinematography.",
                rating = 8.0
            ),

            MoviePost(
                username = "Sam",
                movieName = "Dune: Part Two",
                genre = "Sci-Fi",
                reviewLevel = ReviewLevel.GREAT,
                notes = "The cinematography was incredible.",
                rating = 8.0
            ),

            MoviePost(
                username = "Jordan",
                movieName = "Everything Everywhere All at Once",
                genre = "Comedy",
                reviewLevel = ReviewLevel.GREAT,
                notes = "Creative and surprisingly emotional.",
                rating = 8.0
            ),

            MoviePost(
                username = "Nicole",
                movieName = "Umamusume: Pretty Derby",
                genre = "Animation",
                reviewLevel = ReviewLevel.GREAT,
                notes = "Really fun and memorable.",
                rating = 8.0
            ),

            MoviePost(
                username = "Chris",
                movieName = "Iron Man",
                genre = "Action",
                reviewLevel = ReviewLevel.OK,
                notes = "Still fun, but not one of my favorites.",
                rating = 8.0
            ),

            MoviePost(
                username = "Jamie",
                movieName = "Morbius",
                genre = "Action",
                reviewLevel = ReviewLevel.BAD,
                notes = "Didn't really work for me.",
                rating = 8.0
            )
        )

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerView.adapter =
            MovieAdapter(posts)

        profileButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_homeFragment_to_profileFragment
            )
        }
    }
}