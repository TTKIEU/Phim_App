package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FriendsFragment :
    Fragment(
        R.layout.fragment_friends
    ) {

    private val viewModel:
            FriendsViewModel by viewModels()

    private lateinit var friendsAdapter:
            UserAdapter

    private lateinit var searchAdapter:
            UserAdapter

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
                R.id.friendSearchInput
            )

        val searchButton =
            view.findViewById<
                    Button
                    >(
                R.id.friendSearchButton
            )

        val backButton =
            view.findViewById<
                    ImageButton
                    >(
                R.id.friendsBackButton
            )

        val friendsList =
            view.findViewById<
                    RecyclerView
                    >(
                R.id.friendsRecyclerView
            )

        val searchList =
            view.findViewById<
                    RecyclerView
                    >(
                R.id.userSearchRecyclerView
            )

        friendsAdapter =
            UserAdapter(
                buttonText =
                    "Remove"
            ) {
                    user ->

                viewModel
                    .removeFriend(
                        user
                    )
            }

        searchAdapter =
            UserAdapter(
                buttonText =
                    "Add"
            ) {
                    user ->

                viewModel.addFriend(
                    user
                ) {
                        success ->

                    Toast.makeText(
                        requireContext(),

                        if (success) {
                            "Added ${user.username}"
                        } else {
                            "Could not add user"
                        },

                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        friendsList.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        friendsList.adapter =
            friendsAdapter

        searchList.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        searchList.adapter =
            searchAdapter

        searchButton
            .setOnClickListener {

                viewModel.search(
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

                            viewModel
                                .friends
                                .collect {

                                    friendsAdapter
                                        .submitList(
                                            it
                                        )
                                }
                        }

                        launch {

                            viewModel
                                .searchResults
                                .collect {

                                    searchAdapter
                                        .submitList(
                                            it
                                        )
                                }
                        }
                    }
            }

        viewModel.loadFriends()
    }
}