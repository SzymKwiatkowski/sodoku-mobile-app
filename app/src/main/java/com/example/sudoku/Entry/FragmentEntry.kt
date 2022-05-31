package com.example.sudoku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sudoku.R
import com.example.sudoku.databinding.ActivityMainBinding
import com.example.sudoku.databinding.FragmentEntryBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class FragmentEntry : Fragment(R.layout.fragment_entry) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEntryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_entry, container, false)

        binding.btnStart.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentEntry_to_fragmentStart)
        }

        binding.btnQuit.setOnClickListener{
            exitProcess(0)
        }

        binding.btnTable.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentEntry_to_fragmentTable2)
        }

        return binding.root
    }
}