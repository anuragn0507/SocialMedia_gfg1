package com.anurag.socialmedia_gfg1.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anurag.socialmedia_gfg1.R

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_fragment_container, LoginFragment())
            .commit()
    }
}