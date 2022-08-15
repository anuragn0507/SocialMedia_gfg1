package com.anurag.socialmedia_gfg1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.Comment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.thunder413.datetimeutils.DateTimeStyle
import com.github.thunder413.datetimeutils.DateTimeUtils

class CommentAdapter(options: FirestoreRecyclerOptions<Comment>) :
    FirestoreRecyclerAdapter<Comment, CommentAdapter.CommentsViewHolder>(options) {

    class CommentsViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val  commentText : TextView = itemView.findViewById(R.id.comment_text)
        val  commentAuthor : TextView = itemView.findViewById(R.id.comment_author)
        val  commentTime : TextView = itemView.findViewById(R.id.comment_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item,parent, false)
        return CommentsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int, model: Comment) {
        holder.commentText.text = model.text
        holder.commentAuthor.text = model.user.name

        val date = DateTimeUtils.formatDate(model.time)
        val dateFormatted = DateTimeUtils.formatWithStyle(date, DateTimeStyle.LONG)

        holder.commentTime.text = dateFormatted

    }

}