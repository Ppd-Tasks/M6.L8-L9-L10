package com.example.m6l8.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.m6l8.R
import com.example.m6l8.activity.MainActivity
import com.example.m6l8.model.Post
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter (var activity: MainActivity, var items: ArrayList<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_post_list, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: Post = items[position]
        if (holder is PostViewHolder) {
            holder.tv_title.text = post.title!!.uppercase(Locale.getDefault())
            holder.tv_body.text = post.body
            holder.ll_post.setOnLongClickListener {
                activity.dialogPost(post)
                false
            }

            val iv_photo = holder.iv_photo

            Glide.with(activity)
                .load(post.img)
                .error(R.drawable.im_detault)
                .into(iv_photo)

        }
    }

    inner class PostViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tv_title: TextView
        var tv_body: TextView
        var iv_photo: ImageView
        var ll_post: LinearLayout

        init {
            ll_post = view.findViewById(R.id.ll_post)
            tv_title = view.findViewById(R.id.tv_title)
            tv_body = view.findViewById(R.id.tv_body)
            iv_photo = view.findViewById(R.id.iv_photo)
        }
    }

    init {
        this.activity = activity
        this.items = items
    }
}