package com.example.comment.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.comment.R
import com.example.comment.comment.CommentLVAdapter
import com.example.comment.comment.CommentModel
import com.example.comment.databinding.ActivityBoardInsideBinding
import com.example.comment.utils.FBAuth
import com.example.comment.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardInsideBinding
    //List
    private val TAG = BoardInsideActivity::class.java.simpleName
    private val commentDataList = mutableListOf<CommentModel>()
    private lateinit var  commentAdapter : CommentLVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_inside)
        //댓글 버튼 눌름
        binding.commentBtn.setOnClickListener{
            insertComment()

        }
        //adapter 연결
        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter= commentAdapter

        getCommentData()

    }
    //커멘트 데이터 받아오기
    fun getCommentData(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentDataList.clear()
                for (dataModel in dataSnapshot.children){
                    Log.d(TAG, dataModel.toString())
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }
                //어뎁터 동기화
                commentAdapter.notifyDataSetChanged()
               // Log.d(TAG,commentDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG,"loadPost : onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.addValueEventListener(postListener)

    }
    fun insertComment(){
        val comment = binding.commentArea.text.toString()
        Log.d(TAG, comment)
        //comment
        // boardkey값 넣어서
        // commentkey값
        //commentdata
        FBRef.commentRef

            .push()
            .setValue(
                CommentModel(
                    comment,
                    FBAuth.getTime()

                )
            )
        Toast.makeText(this,"댓글 입력 완료", Toast.LENGTH_SHORT)
        binding.commentArea.setText("")

    }
}