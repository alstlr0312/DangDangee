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

var backFragment = ProfileEditFragment()
class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        return inflater.inflate(R.layout.fragment_profile,container,false)
    }


    fun back(){
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_containers,backFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}