package com.example.dangdangee.Utils

import com.example.dangdangee.auth.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.auth.User
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {

    companion object{
        private lateinit var auth: FirebaseAuth

        fun getUid() : String{
            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }

        fun getEmail() : String{
            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.email.toString()
        }

        fun getDisplayName() : String{
            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.displayName.toString()
        }

        fun getTime() : String{
            val currentDataTime = Calendar.getInstance().time
            val dataFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss",Locale.KOREA).format(currentDataTime)

            return dataFormat
        }

        fun setDisplayName(name:String) {
            auth.currentUser?.updateProfile(userProfileChangeRequest {
                displayName = name
            })
        }

        fun setPassword(pwd:String) {
            auth.currentUser?.updatePassword(pwd)
        }
    }
}