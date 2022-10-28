package com.anurag.socialmedia_gfg1.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anurag.socialmedia_gfg1.MainActivity
import com.anurag.socialmedia_gfg1.R

class AuthenticationActivity : AppCompatActivity(), AuthInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_fragment_container, LoginFragment())
            .commit()
    }

    override fun onSuccessfulAuth() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}