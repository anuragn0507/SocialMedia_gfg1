package com.anurag.socialmedia_gfg1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.User
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(options: FirestoreRecyclerOptions<User>,val context:Context ):FirestoreRecyclerAdapter<User, SearchAdapter.SearchViewHolder>(options) {
        class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val userImage : CircleImageView = itemView.findViewById(R.id.profile_image)
            val nameText : TextView = itemView.findViewById(R.id.user_name)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int, model: User) {
        holder.nameText.text = model.name

        FirebaseFirestore.getInstance().collection("Users")
            .document(snapshots.getSnapshot(position).id).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val user = it.result?.toObject(User::class.java)
                    print(user)
                    Glide.with(context)
                        .load(user?.imageUrl)
                        .placeholder(R.drawable.person_icon_black)
                        .centerCrop()
                        .into(holder.userImage)
                    //add some changes...
                }
            }
    }

}