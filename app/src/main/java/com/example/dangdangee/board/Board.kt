package com.example.dangdangee.board

import  android.content.Context
import android.graphics.drawable.DrawableContainer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.dangdangee.R
import com.example.dangdangee.databinding.ActivityBoardBinding
import com.example.dangdangee.databinding.ActivityIntroBinding

class Board : AppCompatActivity() {
    private lateinit var binding: ActivityBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_board)

        val boardList = mutableListOf<BoardModel>()
        boardList.add(BoardModel("a","b","c","d"))

        val BoardRVAdapter = BoardListLVAdapter(boardList)
        binding.boardListView.adapter = BoardRVAdapter
    }


}