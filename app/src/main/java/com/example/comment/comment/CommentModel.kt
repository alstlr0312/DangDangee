package com.example.comment.comment

import android.graphics.drawable.Drawable

data class CommentModel (
    val commentTitle : String= "",
    val commentCreatedTime : String = "",
    val commentDelete : Drawable ?= null
)