package com.anurag.socialmedia_gfg1.ui

import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.adapters.ChatroomAdapter
import com.anurag.socialmedia_gfg1.models.Chatroom
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ChatroomsFragment : Fragment() {

    private lateinit var chatroomRecyclerView: RecyclerView
    private var chatroomAdapter : ChatroomAdapter? = null

    private lateinit var  createChatroom : FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatrooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatroomRecyclerView = view.findViewById(R.id.chatrooms_rv)

        setupRecyclerView()
        createChatroom = view.findViewById(R.id.create_chatroom)
        setupCreateChatroom()
    }

    private fun setupRecyclerView(){
        val firestore = FirebaseFirestore.getInstance()
        val query  = firestore.collection("Chatrooms").orderBy("name", Query.Direction.ASCENDING)

        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<Chatroom>().setQuery(query, Chatroom:: class.java).build()

        chatroomAdapter = activity?.let {
            ChatroomAdapter(firestoreRecyclerOptions, it)
        }

        chatroomRecyclerView.adapter = chatroomAdapter
        chatroomRecyclerView.layoutManager  = LinearLayoutManager(activity)

    }

    private fun setupCreateChatroom(){
        createChatroom.setOnClickListener {
            val alertDialog : AlertDialog.Builder = AlertDialog.Builder(context)

            val editText = EditText(context)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.marginStart = dpToPixels(8f).toInt()
            params.marginEnd = dpToPixels(8f).toInt()
            editText.layoutParams = params
            editText.setPadding(0, dpToPixels(20f).toInt(), 0, dpToPixels(20f).toInt())

            alertDialog.setTitle("Create Chatroom")
            alertDialog.setMessage("Enter the name of the new chatroom that you want to create: ")

            alertDialog.setView(editText) // parentView.setView(childView)

            var textEntered = ""

            alertDialog.setPositiveButton("Create"){_, _ ->
                textEntered = editText.text.toString()
                val document = FirebaseFirestore.getInstance().collection("Chatrooms").document()
                val chatroom = Chatroom(textEntered, document.id)
                document.set(chatroom)
            }

            alertDialog.setNegativeButton("Cancel"){ dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            alertDialog.show()
        }
    }

    private fun dpToPixels(dp: Float): Float {
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        )
    }

    override fun onStart() {
        super.onStart()
        chatroomAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        chatroomAdapter?.stopListening()
    }

}