package com.example.phim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phim.network.TmdbMovie

class MovieSearchAdapter(
    private val onMovieClick:
        (TmdbMovie) -> Unit
) :
    RecyclerView.Adapter<
            MovieSearchAdapter.MovieViewHolder
            >() {

    private var movies:
            List<TmdbMovie> =
        emptyList()

    fun submitList(
        newMovies:
        List<TmdbMovie>
    ) {

        movies =
            newMovies

        notifyDataSetChanged()
    }

    class MovieViewHolder(
        view: View
    ) :
        RecyclerView.ViewHolder(
            view
        ) {

        val poster:
                ImageView =
            view.findViewById(
                R.id.searchMoviePoster
            )

        val title:
                TextView =
            view.findViewById(
                R.id.searchMovieTitle
            )

        val year:
                TextView =
            view.findViewById(
                R.id.searchMovieYear
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val view =
            LayoutInflater
                .from(
                    parent.context
                )
                .inflate(
                    R.layout.item_movie_search,
                    parent,
                    false
                )

        return MovieViewHolder(
            view
        )
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {

        val movie =
            movies[position]

        holder.title.text =
            movie.title

        holder.year.text =
            movie.releaseDate
                ?.take(4)
                ?: ""

        movie.posterPath
            ?.let {

                Glide.with(
                    holder.itemView
                )
                    .load(
                        "https://image.tmdb.org/t/p/w342$it"
                    )
                    .into(
                        holder.poster
                    )
            }

        holder.itemView
            .setOnClickListener {

                onMovieClick(
                    movie
                )
            }
    }

    override fun getItemCount():
            Int =
        movies.size
}