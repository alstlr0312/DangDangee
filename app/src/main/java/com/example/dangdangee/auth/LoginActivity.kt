package com.example.dangdangee.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.dangdangee.MainActivity
import com.example.dangdangee.R
import com.example.dangdangee.databinding.ActivityLoginBinding
import com.example.dangdangee.map.MainMapActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_login)

        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        binding.loginBtn.setOnClickListener {

            val email = binding.emailArea.text.toString()
            val password = binding.passwordArea.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainMapActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TOP //회원가입하면 뒤에있는 엑티비티 없애기
                        startActivity(intent)
                    } else {
                        Toast.makeText(this,"로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}