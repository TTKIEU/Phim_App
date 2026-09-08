package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeFragment :
    Fragment(
        R.layout.fragment_home
    ) {

    private val viewModel:
            FeedViewModel by viewModels()

    private lateinit var adapter:
            FeedAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        val recyclerView =
            view.findViewById<
                    RecyclerView
                    >(
                R.id.movieRecyclerView
            )

        val profileButton =
            view.findViewById<
                    ImageButton
                    >(
                R.id.navProfileButton
            )

        val friendsButton =
            view.findViewById<
                    ImageButton
                    >(
                R.id.navFriendsButton
            )

        adapter =
            FeedAdapter()

        recyclerView.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        recyclerView.adapter =
            adapter

        profileButton
            .setOnClickListener {

                findNavController()
                    .navigate(
                        R.id.action_homeFragment_to_profileFragment
                    )
            }

        friendsButton
            .setOnClickListener {

                findNavController()
                    .navigate(
                        R.id.action_homeFragment_to_friendsFragment
                    )
            }

        viewLifecycleOwner
            .lifecycleScope
            .launch {

                viewLifecycleOwner
                    .repeatOnLifecycle(
                        Lifecycle.State.STARTED
                    ) {

                        viewModel.feed
                            .collect {

                                adapter.submitList(
                                    it
                                )
                            }
                    }
            }
    }

    override fun onResume() {
        super.onResume()

        viewModel.refresh()
    }
}