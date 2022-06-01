package com.anurag.socialmedia_gfg1.utils

import com.anurag.socialmedia_gfg1.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

object UserUtils {
    var user: User? = null

    fun getCurrentUser(){
        if(FirebaseAuth.getInstance().currentUser !=null){
            FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .get().addOnCompleteListener {
                    if(it.isSuccessful){
                        user = it.result?.toObject(User::class.java)
                    }
                }
        }
    }

}