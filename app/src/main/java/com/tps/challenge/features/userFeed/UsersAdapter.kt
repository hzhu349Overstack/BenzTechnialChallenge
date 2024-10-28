package com.tps.challenge.features.userFeed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tps.challenge.TCApplication.R
import com.tps.challenge.data.model.User

class UsersAdapter(val callback: (Int) -> Unit): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    var users: List<User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class UsersViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = users[position]

        with(holder.itemView) {
            findViewById<TextView>(R.id.url).text = "URL: " + item.url
            findViewById<TextView>(R.id.id).text = "ID: " + item.id.toString()
            setOnClickListener {
                callback(item.id)
            }
            Glide.with(this)
                .load(item.avatar_url)
                .into(findViewById(R.id.image_view))

        }
    }
}
