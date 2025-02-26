package com.example.sudoku.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.sudoku.R
import com.example.sudoku.databinding.FragmentStartBinding
import com.example.sudoku.game.Difficulty
import com.example.sudoku.game.FragmentGameDirections

class FragmentStart : Fragment(R.layout.fragment_start) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStartBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_start, container, false)

        binding.btnEasy.setOnClickListener{
            val action =FragmentStartDirections.actionFragmentStartToFragmentGame2()
            action.difficulty = Difficulty.EASY
            findNavController().navigate(action)
        }

        binding.btnMedium.setOnClickListener{
            val action =FragmentStartDirections.actionFragmentStartToFragmentGame2()
            action.difficulty = Difficulty.MEDIUM
            findNavController().navigate(action)
        }

        binding.btnHard.setOnClickListener{
            val action =FragmentStartDirections.actionFragmentStartToFragmentGame2()
            action.difficulty = Difficulty.HARD
            findNavController().navigate(action)
        }

        return binding.root
    }
}