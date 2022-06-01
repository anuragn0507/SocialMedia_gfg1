package com.anurag.socialmedia_gfg1.models

data class Comment(val text : String= " ",
                   val user: User  = User(),
                   val time: Long = 0L)            {
}