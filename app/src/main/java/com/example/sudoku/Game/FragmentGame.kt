package com.example.sudoku.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku.Cell
import com.example.sudoku.R
import com.example.sudoku.SudokuBoardView
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.view.*

class FragmentGame : Fragment(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: FragmentGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        // setting listener and live data
        view.sudokuBoardView.registerListener(this)
        viewModel = ViewModelProviders.of(this).get(FragmentGameViewModel::class.java)
        viewModel.selectedCellLiveData.observe(viewLifecycleOwner) { cell ->
            view.sudokuBoardView.updateSelectedCellUI(cell.first, cell.second) }
        viewModel.cellsLiveData.observe(viewLifecycleOwner) { updateCells(it) }

        view.textView.visibility = View.VISIBLE
        view.gridLayout.visibility = View.VISIBLE

        // user input
        val buttonsList = listOf(view.oneBtn, view.twoBtn, view.threeBtn, view.fourBtn, view.fiveBtn,
            view.sixBtn, view.sevenBtn, view.eightBtn, view.nineBtn)
        buttonsList.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.handleInput(index + 1)
            }
        }
        
        
        return view
    }

    private fun updateCells(cells: List<Cell>) = cells.let{
        sudokuBoardView.updateCells(cells)
    }

    override fun onCellTouched(row: Int, column: Int) {
        viewModel.updateSelectedCell(row, column)
    }
}