package com.example.dangdangee.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.example.dangdangee.MainActivity
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBAuth
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.auth.LoginActivity
import com.example.dangdangee.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout{
        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        auth = Firebase.auth

        val profileName = binding.profileName
        profileName.setText(FBAuth.getDisplayName())
        val p1 = binding.p1 // profileName이 속한 layout
        val profilePassword = binding.profilePassword
        val p3 = binding.p3 // profilePassword가 속한 layout
        val profileBtn = binding.profileEditBtn

        profilePassword.addTextChangedListener(object :TextWatcher {
            var text = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                text = profilePassword.text.toString()
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                p3.setBackgroundResource(R.drawable.profile_change_context)
                profileBtn.setBackgroundResource(R.drawable.profile_change_btn)
            }
            override fun afterTextChanged(s: Editable?) {
                if(profilePassword.text.toString() == text) {
                    p3.setBackgroundResource(R.drawable.profile_rounded_context)
                    profileBtn.setBackgroundResource(R.drawable.profile_rounded_btn)
                }
            }
        })

        profileName.addTextChangedListener(object :TextWatcher {
            var text = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                text = profileName.text.toString()
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                p1.setBackgroundResource(R.drawable.profile_change_context)
                profileBtn.setBackgroundResource(R.drawable.profile_change_btn)
            }
            override fun afterTextChanged(s: Editable?) {
                if(profileName.text.toString() == text) {
                    p1.setBackgroundResource(R.drawable.profile_rounded_context)
                    profileBtn.setBackgroundResource(R.drawable.profile_rounded_btn)
                }
            }
        })

        profileBtn.setOnClickListener {
            FBAuth.setDisplayName(profileName.text.toString())
            FBAuth.setPassword(profilePassword.text.toString())
        }

        binding.logOutBtn.setOnClickListener {
            val intent = Intent(activity,LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            auth.signOut()
            startActivity(intent)
        }
        return binding.root
    }
}