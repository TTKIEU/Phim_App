package com.example.phim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RankingAdapter :
    RecyclerView.Adapter<
            RankingAdapter.RankingViewHolder
            >() {

    private var items:
            List<MoviePost> = emptyList()

    fun submitList(movies: List<MoviePost>) {
        items = movies
        notifyDataSetChanged()
    }

    class RankingViewHolder(view: View):
        RecyclerView.ViewHolder(view) {
        val rank:
                TextView =
            view.findViewById(
                R.id.profileRankText
            )

        val poster:
                ImageView =
            view.findViewById(
                R.id.profilePoster
            )

        val title:
                TextView =
            view.findViewById(
                R.id.profileMovieTitle
            )

        val score:
                TextView =
            view.findViewById(
                R.id.profileRatingText
            )

        val level:
                TextView =
            view.findViewById(
                R.id.profileReviewLevel
            )

        val notes:
                TextView =
            view.findViewById(
                R.id.profileNotesText
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RankingViewHolder {

        val view =
            LayoutInflater
                .from(
                    parent.context
                )
                .inflate(
                    R.layout.item_profile_rating,
                    parent,
                    false
                )

        return RankingViewHolder(
            view
        )
    }

    override fun onBindViewHolder(
        holder: RankingViewHolder,
        position: Int
    ) {

        val movie =
            items[position]

        holder.rank.text =
            "${position + 1}"

        holder.title.text =
            movie.movieName

        holder.score.text =
            "%.1f".format(
                movie.rating
            )

        holder.level.text =
            movie.reviewLevel

        holder.notes.text =
            if (
                movie.notes.isBlank()
            ) {
                "No notes"
            } else {
                movie.notes
            }

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
    }

    override fun getItemCount():
            Int =
        items.size
}