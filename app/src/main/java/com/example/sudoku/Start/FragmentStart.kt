package com.example.sudoku.Start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.sudoku.R
import com.example.sudoku.databinding.FragmentStartBinding
import kotlin.system.exitProcess

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentStart.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentStart : Fragment(R.layout.fragment_start) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStartBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_start, container, false)

        binding.btnEasy.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentEntry_to_fragmentStart)
        }

        binding.btnMedium.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentEntry_to_fragmentTable2)
        }

        binding.btnHard.setOnClickListener{
            exitProcess(0)
        }

        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentStart_to_fragmentEntry)
        }

        return binding.root
    }
}