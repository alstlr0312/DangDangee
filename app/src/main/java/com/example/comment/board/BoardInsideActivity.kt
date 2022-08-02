package com.example.comment.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private val commentKeyList = mutableListOf<String>()
    private lateinit var  commentAdapter : CommentLVAdapter
    private lateinit var key:String
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
        binding.commentLV.setOnItemClickListener{
                parent,view, position, id->
            //keyList에 있는 key 받아오기
            key = commentKeyList[position]
            //Toast.makeText(this,key,Toast.LENGTH_LONG).show()
            showDialog()

        }

    }
    //다이얼로그 띄우기
    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("삭제하시겠습니까?")
        val alertDialog = mBuilder.show()

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener{
            FBRef.commentRef.child(key).removeValue()
            Toast.makeText(this,"삭제완료",Toast.LENGTH_LONG).show()
            finish()
            val intent = Intent(this, BoardInsideActivity::class.java)
            startActivity(intent)


        }
    }
    //커멘트 데이터 받아오기
    private fun getCommentData() {
        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    commentDataList.clear()
                    for (dataModel in dataSnapshot.children) {
                        Log.d(TAG, dataModel.toString())
                        dataModel.key
                        val item = dataModel.getValue(CommentModel::class.java)
                        commentDataList.add(item!!)
                        commentKeyList.add(dataModel.key.toString())
                    }

                    //어뎁터 동기화
                    commentAdapter.notifyDataSetChanged()

                } catch (e: Exception) {
                    Log.d(TAG, "삭제완료")

                }
            }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost : onCancelled", databaseError.toException())
                }

            }
            FBRef.commentRef.addValueEventListener(postListener)

        }
    //댓글 입력
    private fun insertComment(){
        val comment = binding.commentArea.text.toString()
        val key = FBRef.commentRef.push().key.toString()
        Log.d(TAG, comment)
        //comment
        // boardkey값 넣어서
        // commentkey값
        //commentdata
        FBRef.commentRef
            .child(key)
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