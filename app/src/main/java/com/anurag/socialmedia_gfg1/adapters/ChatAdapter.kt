package com.anurag.socialmedia_gfg1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.Chat
import com.anurag.socialmedia_gfg1.utils.UserUtils
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(options: FirestoreRecyclerOptions<Chat>) : FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder>(options){
    companion object{
        const val MSG_BY_SELF = 0
        const val MSG_BY_OTHER = 1
    }
    class ChatViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val chatText: TextView = itemView.findViewById(R.id.chat_text)
        val authorName : TextView= itemView.findViewById(R.id.chat_author)

    }

    override fun getItemViewType(position: Int): Int {
        if(getItem(position).author.id == UserUtils.user?.id){
            return MSG_BY_SELF
        }else{
            return MSG_BY_OTHER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        var itemView:View? = null
        if(viewType == MSG_BY_SELF){
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.self_chat_item,parent, false)
        }else{
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.other_chat_item,parent, false)
        }
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: Chat) {
        holder.chatText.text = model.text
        holder.authorName.text = model.author.name
    }
}