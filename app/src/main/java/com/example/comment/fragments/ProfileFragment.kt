package com.example.comment.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.FragmentTransaction
import com.example.comment.R
import com.example.comment.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout{
        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.profileEdit.setOnClickListener {
            startActivity(Intent(activity,ProfileEdit::class.java))
        }
        return binding.root
    }

}