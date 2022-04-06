package com.anurag.socialmedia_gfg1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.Post
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.w3c.dom.Text

//FirestoreRecyclerAdapter
class FeedAdapter (options : FirestoreRecyclerOptions<Post>, val context: Context ): FirestoreRecyclerAdapter<Post, FeedAdapter.FeedViewHolder>(options) {


    class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postImage: ImageView = itemView.findViewById(R.id.post_image)
        val postText: TextView = itemView.findViewById(R.id.post_text)
        val authorText: TextView = itemView.findViewById(R.id.post_author)
        val timeText: TextView = itemView.findViewById(R.id.post_time)
        val likeIcon: ImageView = itemView.findViewById(R.id.like_icon)
        val likeCount: TextView = itemView.findViewById(R.id.like_count)
        val commentIcon: ImageView = itemView.findViewById(R.id.comment_icon)
        val commentText: TextView = itemView.findViewById(R.id.comment_count)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int, model: Post) {
        holder.postText.text = model.text
        holder.authorText.text = model.user.name


        Glide.with(context)
            .load(model.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.postImage)

        holder.likeCount.text = model.likesList.size.toString()


        



    }

}