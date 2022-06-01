package com.anurag.socialmedia_gfg1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anurag.socialmedia_gfg1.auth.AuthenticationActivity
import com.anurag.socialmedia_gfg1.ui.ChatroomsFragment
import com.anurag.socialmedia_gfg1.ui.FeedFragment
import com.anurag.socialmedia_gfg1.ui.ProfileFragment
import com.anurag.socialmedia_gfg1.ui.SearchFragment
import com.anurag.socialmedia_gfg1.utils.UserUtils
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if user if logged in
        if(FirebaseAuth.getInstance().currentUser == null){
            val intent = Intent(this, AuthenticationActivity:: class.java)
                startActivity(intent)
                finish()
            }
        UserUtils.getCurrentUser()

        setFragment(FeedFragment())

        val bottomNavigationView: BottomNavigationView= findViewById(R.id.bottom_nav_view)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.feed_item ->{
                    setFragment(FeedFragment())
                }
                R.id.search_item ->{
                    setFragment(SearchFragment())
                }
                R.id.chatrooms ->{
                    setFragment(ChatroomsFragment())
                }
                R.id.profile_item ->{
                   setFragment((ProfileFragment()))
                }
            }
            true
        }
    }

    // we have you the DRY concept so that we need not to reuse the codes so we have created this fun
    // In this function we will pass the fragment,
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .commit()
    }
}