package com.anurag.socialmedia_gfg1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.adapters.CommentAdapter
import com.anurag.socialmedia_gfg1.models.Comment
import com.anurag.socialmedia_gfg1.utils.UserUtils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class CommentsActivity : AppCompatActivity() {
    private var commentsAdapter : CommentAdapter? = null
    private lateinit var commentsRecyclerView: RecyclerView

    private var postId: String? = null

    companion object{
        const val POST_ID ="PostID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        if(intent.hasExtra("PostID")){
            postId = intent.getStringExtra("PostId")
        }

        commentsRecyclerView = findViewById(R.id.comment_rv)
        setupRecyclerView()

        val commentEditText : EditText = findViewById(R.id.enter_comment)
        val sendIcon : ImageView = findViewById(R.id.send_comment)

        sendIcon.setOnClickListener {
            val commentText  = commentEditText.text.toString()

            if(TextUtils.isEmpty(commentText)){
                Toast.makeText(this,"Comment cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val firestore = FirebaseFirestore.getInstance()
            val comment = UserUtils.user?.let{ currentUser ->
                Comment(
                    commentText,
                    currentUser,
                    System.currentTimeMillis()
                )
            }
            postId?.let { postId->
                comment?.let { comment ->
                    firestore.collection("Posts").document(postId).collection("Comments")
                        .document().set(comment)
                }
            }

            commentEditText.text.clear()
        }
    }

    private fun setupRecyclerView() {
        val firestore = FirebaseFirestore.getInstance()
        val query = postId?.let{
            firestore.collection("Posts").document(it).collection("Comments")
        }

        val firestoreRecyclerOptions = query?.let{
            FirestoreRecyclerOptions.Builder<Comment>().setQuery(it, Comment::class.java).build()
        }
        commentsAdapter = firestoreRecyclerOptions?.let {
            CommentAdapter(it)
        }

        commentsRecyclerView.adapter = commentsAdapter
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        commentsAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        commentsAdapter?.stopListening()
    }
}