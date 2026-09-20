@file:Suppress("IllegalIdentifier")
package com.example.phim

import com.example.phim.network.TmdbMovie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieViewModelTest {

    @Test
    fun `selectMovie stores selected movie`() {
        val viewModel =
            MovieViewModel()

        val movie =
            TmdbMovie(
                id = 157336,
                title = "Interstellar",
                posterPath = "/poster.jpg",
                releaseDate = "2014-11-07",
                genreIds = listOf(
                    12,
                    18,
                    878
                )
            )

        viewModel.selectMovie(
            movie
        )

        assertEquals(
            movie,
            viewModel.selectedMovie.value
        )
    }

    @Test
    fun `finishComparisons clears selected movie`() {
        val viewModel =
            MovieViewModel()

        val movie =
            TmdbMovie(
                id = 157336,
                title = "Interstellar",
                posterPath = "/poster.jpg",
                releaseDate = "2014-11-07",
                genreIds = listOf(878)
            )

        viewModel.selectMovie(
            movie
        )

        viewModel.finishComparisons()

        assertNull(
            viewModel.selectedMovie.value
        )
    }

    @Test
    fun `finishComparisons clears current and comparison movies`() {
        val viewModel =
            MovieViewModel()

        viewModel.finishComparisons()

        assertNull(
            viewModel.currentMovie.value
        )

        assertNull(
            viewModel.comparisonMovie.value
        )
    }

    @Test
    fun `new viewModel starts with empty rankings`() {
        val viewModel =
            MovieViewModel()

        assertEquals(
            emptyList<MoviePost>(),
            viewModel.rankings.value
        )
    }

    @Test
    fun `new viewModel starts without selected movie`() {
        val viewModel =
            MovieViewModel()

        assertNull(
            viewModel.selectedMovie.value
        )
    }
}