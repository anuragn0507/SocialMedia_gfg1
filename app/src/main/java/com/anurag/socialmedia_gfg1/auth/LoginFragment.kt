package com.anurag.socialmedia_gfg1.auth

import android.app.FragmentManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.anurag.socialmedia_gfg1.R
import com.google.android.material.textfield.TextInputLayout


class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goToRegister: TextView = view.findViewById(R.id.go_to_register)

        goToRegister.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.replace(R.id.auth_fragment_container, RegisterFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        val emailText:TextInputLayout = view.findViewById(R.id.email_text)
        val passwordText:TextInputLayout = view.findViewById(R.id.password_text)
        val loginButton: Button = view.findViewById(R.id.login_button)
        val loginProgress:TextInputLayout = view.findViewById(R.id.login_progress)

        loginButton.setOnClickListener {
            val email = emailText.editText?.text.toString()
            val password = passwordText.editText?.text.toString()

            emailText.error = null
            passwordText.error = null

            if (TextUtils.isEmpty(email)) {
                emailText.error = "Email is required"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                passwordText.error = "Password is required"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailText.error= "Please enter a valid email address"
                return@setOnClickListener

            }

            loginProgress.visibility = View.VISIBLE
        }
    }
}