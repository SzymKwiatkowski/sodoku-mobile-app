package com.example.sudoku.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sudoku.R
import com.example.sudoku.databinding.FragmentEntryBinding

class FragmentEntry : Fragment(R.layout.fragment_entry) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEntryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_entry, container, false)

        binding.btnStart.setOnClickListener{
            findNavController().navigate(FragmentEntryDirections.actionFragmentEntryToFragmentStart())
        }

        binding.btnTable.setOnClickListener{
            findNavController().navigate(FragmentEntryDirections.actionFragmentEntryToFragmentTable2())
        }

        return binding.root
    }
}