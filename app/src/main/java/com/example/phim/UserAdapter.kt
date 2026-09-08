package com.example.phim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phim.model.User

class UserAdapter(

    private val buttonText:
    String,

    private val onClick:
        (User) -> Unit

) :
    RecyclerView.Adapter<
            UserAdapter.UserViewHolder
            >() {

    private var users:
            List<User> =
        emptyList()

    fun submitList(
        newUsers:
        List<User>
    ) {

        users =
            newUsers

        notifyDataSetChanged()
    }

    class UserViewHolder(
        view: View
    ) :
        RecyclerView.ViewHolder(
            view
        ) {

        val username:
                TextView =
            view.findViewById(
                R.id.userNameText
            )

        val action:
                Button =
            view.findViewById(
                R.id.userActionButton
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {

        val view =
            LayoutInflater
                .from(
                    parent.context
                )
                .inflate(
                    R.layout.item_user,
                    parent,
                    false
                )

        return UserViewHolder(
            view
        )
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {

        val user =
            users[position]

        holder.username.text =
            user.username

        holder.action.text =
            buttonText

        holder.action
            .setOnClickListener {

                onClick(user)
            }
    }

    override fun getItemCount():
            Int =
        users.size
}