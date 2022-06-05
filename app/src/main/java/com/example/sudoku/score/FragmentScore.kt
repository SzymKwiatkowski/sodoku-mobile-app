package com.example.sudoku.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.sudoku.R
import com.example.sudoku.databinding.FragmentScoreBinding

class FragmentScore : Fragment() {
    private lateinit var viewModel: FragmentScoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentScoreBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)
        viewModel = ViewModelProviders.of(this).get(FragmentScoreViewModel::class.java)
        viewModel.score.postValue(FragmentScoreArgs.fromBundle(requireArguments()).score)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.scoreViewModel = viewModel
        viewModel.score.observe(viewLifecycleOwner) { binding.scoreTextView.text = it.toString() }

        binding.btnTableScore.setOnClickListener {
            findNavController().navigate(FragmentScoreDirections.actionFragmentScoreToFragmentTable())
        }

        return binding.root
    }
}