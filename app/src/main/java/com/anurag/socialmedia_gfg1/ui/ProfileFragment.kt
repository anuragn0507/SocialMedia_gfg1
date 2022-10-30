package com.anurag.socialmedia_gfg1.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.anurag.socialmedia_gfg1.R
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {
    private lateinit var userImage: CircleImageView
    private var imageUri: Uri?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userImage = view.findViewById(R.id.user_image)
        val userName: EditText = view.findViewById(R.id.user_name)

    }


}