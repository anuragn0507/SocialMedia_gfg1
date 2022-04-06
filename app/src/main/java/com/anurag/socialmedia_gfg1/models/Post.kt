package com.anurag.socialmedia_gfg1.models

class Post(val text: String = " ",
           val imageUrl: String? = null,
            val user: User = User(),
            val time: Long = 0L,
            val likesList: MutableList<String> = mutableListOf()) {
}