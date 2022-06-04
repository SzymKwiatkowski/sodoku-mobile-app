package com.example.sudoku.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku.Cell
import com.example.sudoku.SudokuBoard

class FragmentGameViewModel : ViewModel() {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    var scoreLiveData = MutableLiveData<Int>()

    private var selectedRow = -1
    private var selectedColumn = -1

    private val board: SudokuBoard

    init {
        val cells = List((9*9)) {i -> Cell(i / 9, i % 9, i % 9) }
        board = SudokuBoard(9, cells)
        cells[11].isStartingCell = true
        cells[21].isStartingCell = true

        selectedCellLiveData.postValue(Pair(selectedRow, selectedColumn))
        scoreLiveData.postValue(0)
        cellsLiveData.postValue(board.cells)
    }

    fun handleInput(number: Int){
        if (selectedColumn == -1 || selectedRow == -1) return
        if (board.getCell(selectedRow, selectedColumn).isStartingCell) return

        board.getCell(selectedRow, selectedColumn).value = number
        cellsLiveData.postValue(board.cells)
    }

    fun updateSelectedCell(row: Int, column: Int){
        if (!board.getCell(row, column).isStartingCell) {
            selectedRow = row
            selectedColumn = column
            selectedCellLiveData.postValue(Pair(row, column))
        }
    }

    fun incrementScore(){
        scoreLiveData.postValue(scoreLiveData.value?.plus(10))
    }
}