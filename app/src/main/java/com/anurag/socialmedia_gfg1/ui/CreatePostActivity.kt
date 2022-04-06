package com.anurag.socialmedia_gfg1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.Post
import com.anurag.socialmedia_gfg1.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreatePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val enterText: EditText = findViewById(R.id.enter_text)
        val postButton: Button = findViewById(R.id.post_button)

        postButton.setOnClickListener {
            val text = enterText.text.toString()

            if(TextUtils.isEmpty(text)){
                Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            addPost(text)
        }
    }

    private fun addPost(text: String){
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = task.result?.toObject(User::class.java)

                    val post = user?.let {
                        Post(text, "", it, System.currentTimeMillis())
                        }

                    post?.let {
                        firestore.collection("Posts").document().set(it)
                            .addOnCompleteListener { postTask ->
                                if(postTask.isSuccessful){
                                    Toast.makeText(this,"Posted Successfully", Toast.LENGTH_LONG).show()
                                    finish()
                                }else{
                                    Toast.makeText(this, "Error Occurred. Please try again.", Toast.LENGTH_LONG).show()
                                }

                            }
                    }
                } else{
                    Toast.makeText(this, "Error Occurred. Please try again.", Toast.LENGTH_LONG).show()
                }
            }
    }

}