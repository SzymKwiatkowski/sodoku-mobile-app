package com.example.sudoku.Game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku.R
import com.example.sudoku.SudokuBoardView
import com.example.sudoku.databinding.ActivityMainBinding.inflate
import com.example.sudoku.databinding.FragmentEntryBinding
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.view.*

class FragmentGame : Fragment(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: FragmentGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val viewRoot = LayoutInflater.from(this).inflate(R.layout.fragment_game, container, false)
//        val binding: ViewDataBinding? = DataBindingUtil.bind(viewRoot)

        val view = inflater.inflate(R.id.sudokuBoardView, container, false)
        val binding: FragmentEntryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_entry, container, false)
//
//        val binding: SudokuBoardView = SudokuBoardView.inflate(getLayoutInflater(), view, false)
//        binding.
        view.sudokuBoardView.registerListener(this)
        viewModel = ViewModelProviders.of(this).get(FragmentGameViewModel::class.java)
        viewModel.selectedCellLiveData.observe(viewLifecycleOwner) { cell ->
            sudokuBoardView.updateSelectedCellUI(cell.first, cell.second) }
        return binding.root
    }

    override fun onCellTouched(row: Int, column: Int) {
        viewModel.updateSelectedCell(row, column)
    }
}