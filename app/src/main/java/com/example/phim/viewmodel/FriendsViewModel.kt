package com.example.phim

import androidx.lifecycle.ViewModel
import com.example.phim.model.User
import com.example.phim.repository.FriendsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FriendsViewModel :
    ViewModel() {

    private val repository =
        FriendsRepository()

    private val _friends =
        MutableStateFlow<List<User>>(
            emptyList()
        )

    val friends:
            StateFlow<List<User>> =
        _friends.asStateFlow()

    private val _searchResults =
        MutableStateFlow<List<User>>(
            emptyList()
        )

    val searchResults:
            StateFlow<List<User>> =
        _searchResults.asStateFlow()

    fun loadFriends() {

        repository
            .loadFriends {

                _friends.value =
                    it
            }
    }

    fun search(
        query: String
    ) {

        repository
            .searchUsers(
                query
            ) {

                _searchResults.value =
                    it
            }
    }

    fun addFriend(
        user: User,
        onComplete:
            (Boolean) -> Unit
    ) {

        repository
            .addFriend(
                user
            ) {

                if (it) {
                    loadFriends()
                }

                onComplete(it)
            }
    }

    fun removeFriend(
        user: User
    ) {

        repository
            .removeFriend(
                user.id
            ) {

                if (it) {
                    loadFriends()
                }
            }
    }
}