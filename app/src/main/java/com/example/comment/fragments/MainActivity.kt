package com.example.comment.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.comment.R
import com.example.comment.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

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

        //처음뜨는 fragment 설정
        supportFragmentManager.beginTransaction().add(R.id.bottom_containers,profileFragment).commit()
        val navigationBarView = binding.bottomNavigationview
        navigationBarView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_map ->{
                    return@OnItemSelectedListener true
                }
                R.id.bottom_search ->{
                    return@OnItemSelectedListener true
                }
                R.id.bottom_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers,profileFragment).commit()
                    return@OnItemSelectedListener true
                }
            }
            false
        })




    }
}