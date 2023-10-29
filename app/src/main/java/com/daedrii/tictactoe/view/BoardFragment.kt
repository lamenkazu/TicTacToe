package com.daedrii.tictactoe.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import com.daedrii.tictactoe.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {

    private lateinit var binding: FragmentBoardBinding

    private var boardList = mutableListOf<Button>()

    val bindingReady = MutableLiveData<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoardBinding.inflate(inflater, container, false)

        bindingReady.value = true

        return binding.root
    }

    fun getBoardList(): MutableList<Button> {
        return boardList
    }

    fun getBinding(): FragmentBoardBinding{
        return binding
    }


}