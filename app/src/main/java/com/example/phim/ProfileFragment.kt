package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.profileRecyclerView)

        val addButton =
            view.findViewById<ImageButton>(R.id.addRankingButton)

        val rankings = listOf(
            MoviePost(
                username = "Taylin",
                movieName = "Spider-Man: No Way Home",
                rating = 9.8
            ),

            MoviePost(
                username = "Taylin",
                movieName = "Interstellar",
                rating = 9.5
            ),

            MoviePost(
                username = "Taylin",
                movieName = "The Batman",
                rating = 8.7
            ),

            MoviePost(
                username = "Taylin",
                movieName = "Dune: Part Two",
                rating = 8.2
            )
        )

        // Highest rated → lowest rated
        val sortedRankings =
            rankings.sortedByDescending { it.rating }

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerView.adapter =
            MovieAdapter(sortedRankings)

        addButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_rankingFragment
            )
        }
    }
}