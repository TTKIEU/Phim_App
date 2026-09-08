package com.example.phim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

class FeedAdapter :
    RecyclerView.Adapter<
            FeedAdapter.FeedViewHolder
            >() {

    private var items:
            List<MoviePost> =
        emptyList()

    fun submitList(
        newItems: List<MoviePost>
    ) {

        items =
            newItems

        notifyDataSetChanged()
    }

    class FeedViewHolder(
        view: View
    ) :
        RecyclerView.ViewHolder(
            view
        ) {

        val poster:
                ImageView =
            view.findViewById(
                R.id.feedPoster
            )

        val activityText:
                TextView =
            view.findViewById(
                R.id.feedActivityText
            )

        val rating:
                TextView =
            view.findViewById(
                R.id.feedRatingText
            )

        val notes:
                TextView =
            view.findViewById(
                R.id.feedNotesText
            )

        val time:
                TextView =
            view.findViewById(
                R.id.feedTimeText
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedViewHolder {

        val view =
            LayoutInflater
                .from(
                    parent.context
                )
                .inflate(
                    R.layout.item_feed_rating,
                    parent,
                    false
                )

        return FeedViewHolder(
            view
        )
    }

    override fun onBindViewHolder(
        holder: FeedViewHolder,
        position: Int
    ) {

        val post =
            items[position]

        holder.activityText.text =
            "${post.username} rated ${post.movieName}"

        holder.rating.text =
            "%.1f".format(
                post.rating
            )

        holder.notes.text =
            if (
                post.notes.isBlank()
            ) {
                "No notes"
            } else {
                "\"${post.notes}\""
            }

        holder.time.text =
            relativeTime(
                post.createdAt
            )

        if (
            post.posterPath != null
        ) {

            Glide.with(
                holder.itemView
            )
                .load(
                    "https://image.tmdb.org/t/p/w342${post.posterPath}"
                )
                .into(
                    holder.poster
                )
        }
    }

    override fun getItemCount():
            Int =
        items.size

    private fun relativeTime(
        timestamp: Long
    ): String {

        val difference =
            System.currentTimeMillis() -
                    timestamp

        val minutes =
            TimeUnit.MILLISECONDS
                .toMinutes(
                    difference
                )

        val hours =
            TimeUnit.MILLISECONDS
                .toHours(
                    difference
                )

        val days =
            TimeUnit.MILLISECONDS
                .toDays(
                    difference
                )

        return when {

            minutes < 1 ->
                "Just now"

            minutes < 60 ->
                "${minutes}m ago"

            hours < 24 ->
                "${hours}h ago"

            else ->
                "${days}d ago"
        }
    }
}