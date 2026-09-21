package com.example.phim

import androidx.lifecycle.ViewModel
import com.example.phim.model.User
import com.example.phim.repository.FriendsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//NOT SET UP FOR DEPENDENCY INJECTION
class FriendsViewModel :
    ViewModel() {

    private val repository =
        FriendsRepository()

    //create a list of friends of type user
    private val _friends = MutableStateFlow<List<User>>(emptyList())

    val friends: StateFlow<List<User>> = _friends.asStateFlow()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())

    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()

    fun loadFriends() { repository.loadFriends { _friends.value = it }
    }

    //search for the user
    //hand over query (name) to repository
    //set the value of the search results to the returned list of users
    fun search(
        query: String
    ) {
        repository.searchUsers(query) { _searchResults.value = it }
    }

    //given user that you'd want to add
    //take in a true or false
    fun addFriend(
        user: User,
        onComplete: (Boolean) -> Unit
    ) {
        //invoke repository add friend function
        //aka adding user to current user's friends list
        //if function went through (returned true), reload friends
        repository.addFriend(user) {if (it) { loadFriends() }
            //tell fragment that it's completed and return true or false
            //if the function went through
            //function takes in bool to pass through and returns nothing HERE!
                onComplete(it)
            }
    }

    //simple remove friend (similar behavior to addfriend
    fun removeFriend(
        user: User
    ) {
        repository.removeFriend(user.id) {
                if (it) { loadFriends() }
            }
    }
}