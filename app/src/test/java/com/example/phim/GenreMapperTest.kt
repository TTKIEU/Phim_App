@file:Suppress("IllegalIdentifier")
package com.example.phim

import org.junit.Assert.assertEquals
import org.junit.Test

class GenreMapperTest {

    @Test
    fun `fromIds returns one genre for one known id`() {
        val result = GenreMapper.fromIds(
            listOf(28)
        )

        assertEquals(
            "Action",
            result
        )
    }

    @Test
    fun `fromIds returns two genres when multiple known ids exist`() {
        val result = GenreMapper.fromIds(
            listOf(
                28,
                12
            )
        )

        assertEquals(
            "Action, Adventure",
            result
        )
    }

    @Test
    fun `fromIds only returns first two genres`() {
        val result = GenreMapper.fromIds(
            listOf(
                28,
                12,
                35
            )
        )

        assertEquals(
            "Action, Adventure",
            result
        )
    }

    @Test
    fun `fromIds ignores unknown genre ids`() {
        val result = GenreMapper.fromIds(
            listOf(
                999999,
                28
            )
        )

        assertEquals(
            "Action",
            result
        )
    }

    @Test
    fun `fromIds returns Other when no known genres exist`() {
        val result = GenreMapper.fromIds(
            listOf(
                999999
            )
        )

        assertEquals(
            "Other",
            result
        )
    }

    @Test
    fun `fromIds returns Other for empty list`() {
        val result =
            GenreMapper.fromIds(
                emptyList()
            )

        assertEquals(
            "Other",
            result
        )
    }
}