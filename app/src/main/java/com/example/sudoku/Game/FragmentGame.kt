package com.example.sudoku.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku.Cell
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
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        view.sudokuBoardView.registerListener(this)
        viewModel = ViewModelProviders.of(this).get(FragmentGameViewModel::class.java)
        viewModel.selectedCellLiveData.observe(viewLifecycleOwner) { cell ->
            view.sudokuBoardView.updateSelectedCellUI(cell.first, cell.second) }
        viewModel.cellsLiveData.observe(viewLifecycleOwner) { updateCells(it) }

        view.progressBar.progress = 10
        return view
    }

    private fun updateCells(cells: List<Cell>) = cells.let{
        sudokuBoardView.updateCells(cells)
    }

    override fun onCellTouched(row: Int, column: Int) {
        viewModel.updateSelectedCell(row, column)
    }

//    private fun buttonsOnClick(number: Int) {
//        viewModel.handleInput(number)
//    }
}