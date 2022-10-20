package com.anurag.socialmedia_gfg1.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anurag.socialmedia_gfg1.MainActivity
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.Chatroom
import com.anurag.socialmedia_gfg1.ui.ChatFragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatroomAdapter(options: FirestoreRecyclerOptions<Chatroom>, val context: Context): FirestoreRecyclerAdapter<Chatroom, ChatroomAdapter.ChatroomViewHolder>(options) {

    class ChatroomViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val nameText: TextView = itemview.findViewById(R.id.chatroom_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chatroom_item,parent, false)
        return ChatroomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatroomViewHolder, position: Int, model: Chatroom) {
        holder.nameText.text = model.name

        holder.itemView.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("ChatroomID", model.id)

            val chatFragment = ChatFragment()
            chatFragment.arguments = bundle
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, chatFragment)
                .addToBackStack(null)
                .commit()


        }
    }

}