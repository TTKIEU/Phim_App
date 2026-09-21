package com.example.phim

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phim.network.TmdbMovie
import com.example.phim.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//NO DEPENDENCY INJECTION // TODO
class SearchViewModel : ViewModel() {

    private val repository =
        MovieRepository()

    private val _movies = MutableStateFlow<List<TmdbMovie>>(emptyList())
    val movies: StateFlow<List<TmdbMovie>> = _movies.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun search(query: String) {
        //blank check
        //return no results
        if (query.isBlank()) { _movies.value = emptyList()
            return
        }
        //launch coroutine
        //needed b/c we're doing network work with TMDB in repository
        viewModelScope.launch {
            //search is currently in progress
            _loading.value = true
            //prepare for failure from TMDB side -> return empty list
            //if failure occurs, log it
            //TODO tell user failure based on error code
            try {
                //hand over work to repository to request from TMDB
                val results =
                    repository.searchMovies(query)
                Log.d(
                    "TMDB_SEARCH",
                    "Found ${results.size} movies"
                )

                _movies.value =
                    results

            } catch (exception: Exception) {
                Log.e(
                    "TMDB_SEARCH",
                    "Movie search failed",
                    exception)
                _movies.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}