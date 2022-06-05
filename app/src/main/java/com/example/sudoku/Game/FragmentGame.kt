package com.example.sudoku.game

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.sudoku.Cell
import com.example.sudoku.R
import com.example.sudoku.SudokuBoardView
import com.example.sudoku.score.FragmentScoreArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.view.*
import java.io.IOException

class FragmentGame : Fragment(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: FragmentGameViewModel
    private lateinit var buttonsList: List<Button>
    private lateinit var notesBtn: ImageButton

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
        viewModel.isTakingNotesLiveData.observe(viewLifecycleOwner) { updateNoteTakingUI(it) }
        viewModel.highlightedKeysLiveData.observe(viewLifecycleOwner) { updateHighlightKeysUI(it) }
        viewModel.scoreLiveData.observe(viewLifecycleOwner) { view.textView.text = it.toString() }
        viewModel.progressLiveData.observe(viewLifecycleOwner) { view.progressBar.progress = (it * 100).toInt()}
        viewModel.gameEndLiveData.observe(viewLifecycleOwner) { if(it) onGameFinished()}

        val listBoard = JSONListFromAsset()
        viewModel.createBoard(listBoard, FragmentGameArgs.fromBundle(requireArguments()).difficulty)
        // user input
        buttonsList = listOf(view.oneBtn, view.twoBtn, view.threeBtn, view.fourBtn, view.fiveBtn,
            view.sixBtn, view.sevenBtn, view.eightBtn, view.nineBtn)
        buttonsList.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.handleInput(index + 1)
            }
        }
        notesBtn = view.btnNotes
        notesBtn.setOnClickListener {
            viewModel.changedNoteTakingState()
        }

        return view
    }

    private fun updateCells(cells: List<Cell>) = cells.let{
        sudokuBoardView.updateCells(cells)
    }

    private fun updateNoteTakingUI(isNoteTaking: Boolean) = isNoteTaking.let {
        if (it) {
            notesBtn.setBackgroundColor(Color.MAGENTA)
        } else {
            notesBtn.setBackgroundColor(Color.LTGRAY)
        }
    }

    private fun JSONListFromAsset(): List<Int>{
        var json: String? = null

        try {
            json = context?.assets?.open("boards.json")?.bufferedReader().use {
                it?.readText()
            }
        } catch (ioException: IOException) {
        }

        val gson = Gson()
        val listIntType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(json, listIntType)
    }

    private fun updateHighlightKeysUI(set: Set<Int>) = set.let {
        buttonsList.forEachIndexed {index, button ->
            val color = if (set.contains(index+1)) Color.parseColor("#3C256F") else Color.LTGRAY
            button.setBackgroundColor(color)
        }
    }

    override fun onCellTouched(row: Int, column: Int) {
        viewModel.updateSelectedCell(row, column)
    }

    private fun onGameFinished(){
        val action = FragmentGameDirections.actionFragmentGameToFragmentScore()
        action.score = viewModel.scoreLiveData.value!!
        findNavController().navigate(action)
    }
}