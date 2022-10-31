package com.anurag.socialmedia_gfg1.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.User
import com.anurag.socialmedia_gfg1.utils.UserUtils
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {
    private lateinit var userImage: CircleImageView
    private var imageUri: Uri?= null
    private var callback:LogOutInterface?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as LogOutInterface
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userImage = view.findViewById(R.id.user_image)
        val userName: EditText = view.findViewById(R.id.user_name)
        val userBio: EditText = view.findViewById(R.id.user_bio)
        val saveButton: EditText = view.findViewById(R.id.save_button)
        val logoutButton: EditText = view.findViewById(R.id.user_name)

        userName.setText(UserUtils.user?.name)
        userBio.setText(UserUtils.user?.bio)

        userImage.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        Glide.with(requireContext())
            .load(UserUtils.user?.imageUrl)
            .placeholder(R.drawable.person_icon_black)
            .centerCrop()
            .into(userImage)

        saveButton.setOnClickListener {
            val newUserName = userName.text.toString()
            val newBio = userBio.text.toString()

            if (newUserName.isBlank()){
                Toast.makeText(context, "Name field cannot be blank", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val userDocument = FirebaseFirestore.getInstance().collection("Users")
                .document(UserUtils.user?.id.toString())

            val user = User(id= UserUtils.user?.id.toString(), name = newUserName, email = UserUtils.user?.email.toString(), bio= newBio)
            userDocument.set(user).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(context,"Profile Updated", Toast.LENGTH_LONG).show()
                    UserUtils.getCurrentUser()
                }else{
                    Toast.makeText(context, "Something went wrong. Please try again later", Toast.LENGTH_LONG).show()
                }
            }
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            callback?.logout()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val fileUri = data?.data
            userImage.setImageURI(fileUri)
            imageUri = fileUri
            addUserImage()
        }else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "Something went wrong. Please try again ", Toast.LENGTH_LONG).show()
        }
    }
    private fun addUserImage(){
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users")
            .document(UserUtils.user?.id.toString())
            .get().addOnCompleteListener {
                val storage = FirebaseStorage.getInstance().reference.child("Images")
                    .child(UserUtils.user?.id.toString() + ".jpg")

                val uploadTask = imageUri?.let { it1 -> storage.putFile(it1) }

                uploadTask?.continueWithTask{task ->
                    if(!task.isSuccessful){
                        Log.e("Upload Task", task.exception.toString())
                        task.exception?.let {
                            throw it
                        }
                    }
                    storage.downloadUrl
                }?.addOnCompleteListener { urlTaskCompleted ->
                    if(urlTaskCompleted.isSuccessful){
                        val downloadUri = urlTaskCompleted.result
                        val newUser = User(id= UserUtils.user?.id)
                    }
                }
            }
    }

}