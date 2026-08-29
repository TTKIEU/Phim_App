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

        // Sample posts for the home feed
        val posts = listOf(

            MoviePost(
                username = "Taylin",
                movieName = "Interstellar",
                rating = 9.5,
                genre = "Sci-Fi",
                reviewLevel = "Great",
                notes = "Amazing visuals and story."
            ),

            MoviePost(
                username = "Alex",
                movieName = "The Batman",
                rating = 8.7,
                genre = "Action",
                reviewLevel = "Great",
                notes = "Really enjoyed the atmosphere."
            ),

            MoviePost(
                username = "Sam",
                movieName = "Dune: Part Two",
                rating = 9.2,
                genre = "Sci-Fi",
                reviewLevel = "Great",
                notes = "The cinematography was incredible."
            ),

            MoviePost(
                username = "Jordan",
                movieName = "Everything Everywhere All at Once",
                rating = 9.6,
                genre = "Comedy",
                reviewLevel = "Great",
                notes = "Unique and surprisingly emotional."
            ),

            MoviePost(
                username = "Nicole",
                movieName = "Umamusume",
                rating = 10.0,
                genre = "Animation",
                reviewLevel = "Great",
                notes = "Absolutely loved it."
            )
        )

        // Set up RecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerView.adapter =
            MovieAdapter(posts)


        // Profile button
        profileButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_homeFragment_to_profileFragment
            )
        }
    }
}