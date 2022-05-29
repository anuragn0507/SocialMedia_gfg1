package com.anurag.socialmedia_gfg1.ui

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.Post
import com.anurag.socialmedia_gfg1.models.User
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreatePostActivity : AppCompatActivity() {
    private lateinit var selectImage : ImageView

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        selectImage = findViewById(R.id.select_image)

        val enterText: EditText = findViewById(R.id.enter_text)
        val postButton: Button = findViewById(R.id.post_button)

        selectImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080,1080)
                .start()
        }

        postButton.setOnClickListener {
            val text = enterText.text.toString()

            if(TextUtils.isEmpty(text)){
                Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            addPost(text)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){
            Activity.RESULT_OK ->{
                val fileuri = data?.data
                selectImage.setImageURI(fileuri)
                imageUri = fileuri
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this,ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this,"Task cancelled or some error occurred", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun addPost(text: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.toObject(User::class.java)

                    val storage = FirebaseStorage.getInstance().reference.child("Images")
                        .child(FirebaseAuth.getInstance().currentUser?.email.toString() + "_"
                                + System.currentTimeMillis() + ".jpg")

                    val uploadTask = imageUri?.let {
                        storage.putFile(it)
                    }

                    uploadTask?.continueWithTask { task1 ->
                        if (!task1.isSuccessful) {
                            Log.d("UploadTask", task1.exception.toString())
                            task1.exception?.let {
                                throw it
                            }
                        }
                        storage.downloadUrl
                    }?.addOnCompleteListener { urlTask ->
                        if (urlTask.isSuccessful) {
                            val downloadUri = urlTask.result

                            val post = user?.let {
                                Post(text, downloadUri.toString(), it, System.currentTimeMillis())
                            }

                            post?.let {
                                firestore.collection("Posts").document().set(it)
                                    .addOnCompleteListener { postTask ->
                                        if (postTask.isSuccessful) {
                                            Toast.makeText(this, "Posted successfully", Toast.LENGTH_LONG).show()
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Error occurred. Please try again.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Error occurred. Please try again.", Toast.LENGTH_LONG).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Error occurred. Please try again.", Toast.LENGTH_LONG).show()
                }
            }
    }
}