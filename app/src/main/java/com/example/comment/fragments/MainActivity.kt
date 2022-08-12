package com.example.comment.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.comment.R
import com.example.comment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val profileFragment: ProfileFragment by lazy {
        ProfileFragment()
    }
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)





    }
}