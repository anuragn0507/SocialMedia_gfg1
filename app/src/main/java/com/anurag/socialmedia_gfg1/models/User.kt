package com.anurag.socialmedia_gfg1.models

data class User(val id: String? = "",
                val name: String = "",
                val email:String = "",
                val bio : String = "",
                val imageUrl: String? = null)
