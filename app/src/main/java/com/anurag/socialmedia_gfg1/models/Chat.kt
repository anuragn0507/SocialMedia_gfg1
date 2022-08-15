package com.anurag.socialmedia_gfg1.models

data class Chat(val text:String = "",
                val author:User = User(),
                val time:Long = 0L,
                val chatroomId: String = " ")
