package com.example.phim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private val posts: List<MoviePost>
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val movieText: TextView =
            view.findViewById(R.id.movieText)

        val ratingText: TextView =
            view.findViewById(R.id.ratingText)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_movie,
                parent,
                false
            )

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {

        val post = posts[position]

        holder.movieText.text =
            "${post.username} has just ranked ${post.movieName}"

        holder.ratingText.text =
            post.rating.toString()
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}