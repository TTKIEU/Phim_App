package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phim.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfileFragment :
    Fragment(
        R.layout.fragment_profile
    ) {

    private val viewModel:
            MovieViewModel by activityViewModels()

    private lateinit var adapter:
            RankingAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        val rankingsView =
            view.findViewById<
                    RecyclerView
                    >(
                R.id.profileRecyclerView
            )

        val addButton =
            view.findViewById<
                    ImageButton
                    >(
                R.id.addRankingButton
            )

        val backButton =
            view.findViewById<
                    ImageButton
                    >(
                R.id.backToHomeButton
            )

        val logoutButton =
            view.findViewById<
                    Button
                    >(
                R.id.logoutButton
            )

        adapter =
            RankingAdapter()

        rankingsView.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        rankingsView.adapter =
            adapter

        addButton
            .setOnClickListener {
                findNavController()
                    .navigate(
                        R.id.action_profileFragment_to_movieSearchFragment
                    )
            }

        backButton
            .setOnClickListener {
                findNavController()
                    .popBackStack()
            }

        logoutButton
            .setOnClickListener {

                AuthRepository().logout()
                viewModel.clearUserData()

                findNavController().navigate(
                    R.id.action_profileFragment_to_loginFragment
                )
            }

        viewLifecycleOwner
            .lifecycleScope
            .launch {

                viewLifecycleOwner
                    .repeatOnLifecycle(
                        Lifecycle.State.STARTED
                    ) {

                        viewModel.rankings
                            .collect {

                                adapter
                                    .submitList(
                                        it
                                    )
                            }
                    }
            }

        viewModel.loadRankings()
    }
}