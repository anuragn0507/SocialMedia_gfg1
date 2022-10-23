package com.anurag.socialmedia_gfg1.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.anurag.socialmedia_gfg1.MainActivity
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.adapters.SearchAdapter
import com.anurag.socialmedia_gfg1.models.User
import com.anurag.socialmedia_gfg1.utils.UserUtils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore


class SearchFragment : Fragment() {
    private  var searchAdapter: SearchAdapter?= null
    private lateinit var searchRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar= view.findViewById(R.id.search_toolbar)
        toolbar.title = "Search Users"

        (activity as? MainActivity)?.setSupportActionBar(toolbar)
        (activity as? MainActivity)?.supportActionBar?.show()
        setHasOptionsMenu(true)

        val firestore = FirebaseFirestore.getInstance()
        val query = firestore.collection("Users")
            .whereNotEqualTo("id", UserUtils.user?.id)

        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java).build()
        // Read about pagination

       context?.let{
           searchAdapter = SearchAdapter(firestoreRecyclerOptions, it)
       }
    }
}