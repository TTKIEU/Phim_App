package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: MovieViewModel by activityViewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.profileRecyclerView)

        val addButton =
            view.findViewById<ImageButton>(R.id.addRankingButton)

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
//        updateRecyclerView(recyclerView)

        addButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_rankingFragment
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ){
                viewModel.rankings.collect { rankings ->
                    recyclerView.adapter = MovieAdapter(rankings)
                }
            }
        }
    }
}