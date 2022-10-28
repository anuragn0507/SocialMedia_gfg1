package com.anurag.socialmedia_gfg1.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.anurag.socialmedia_gfg1.MainActivity
import com.anurag.socialmedia_gfg1.R
import com.anurag.socialmedia_gfg1.models.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment() {

    companion object{
        const val TAG = "RegisterFragment"
    }

    private var callback: AuthInterface?=null

    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as AuthInterface
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goToLogin: TextView = view.findViewById(R.id.go_to_login)

        goToLogin.setOnClickListener{
            fragmentManager?.beginTransaction()
                ?.replace(R.id.auth_fragment_container, LoginFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        val emailText: TextInputLayout = view.findViewById(R.id.email_text)
        val passwordText: TextInputLayout = view.findViewById(R.id.password_text)
        val confirmPasswordText: TextInputLayout = view.findViewById(R.id.confirm_password_text)
        val nameText: TextInputLayout = view.findViewById(R.id.name_text)
        val registerButton: Button = view.findViewById(R.id.register_button)
        val registerProgress: ProgressBar = view.findViewById(R.id.register_progress)

        registerButton.setOnClickListener {
            val email = emailText.editText?.text.toString()
            val name = nameText.editText?.text.toString()
            val password = passwordText.editText?.text.toString()
            val confirmPassword = confirmPasswordText.editText?.text.toString()

            emailText.error = null
            nameText.error = null
            passwordText.error = null
            confirmPasswordText.error = null

            if(TextUtils.isEmpty(email)){
                emailText.error = "Email is required"
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(name)){
                nameText.error = "Name is required"
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password)){
                passwordText.error = "Password is required"
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(confirmPassword)){
                confirmPasswordText.error = "Confirm Password is required"
                return@setOnClickListener
            }

            if(password != confirmPassword){
                confirmPasswordText.error = "Password do not match"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailText.error = "Please enter a valid e-mail address"
                return@setOnClickListener
            }

            if(!password.matches(passwordRegex)){
               passwordText.error = "Password should contain Minimum eight characters, at least one uppercase letter, one lowercase letter and one number"
                return@setOnClickListener
            }

            registerProgress.visibility = View.VISIBLE
            val auth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                       val user = User(auth.currentUser?.uid, name , email)
                        val firestore = FirebaseFirestore.getInstance().collection("Users")
                        firestore.document(auth.currentUser?.uid.toString()).set(user)
                            .addOnCompleteListener {task2 ->
                                registerProgress.visibility = View.GONE
                                if(task2.isSuccessful) {
                                    callback?.onSuccessfulAuth()
                                }else{
                                    Toast.makeText(activity, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
                                    Log.e(LoginFragment.TAG, task.exception.toString())
                                }
                            }

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)

                    }else{
                        Toast.makeText(activity, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
                        Log.e(LoginFragment.TAG, task.exception.toString())
                    }
                }


        }


    }


}

//