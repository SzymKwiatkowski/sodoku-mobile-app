package com.example.sudoku.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku.Cell
import com.example.sudoku.SudokuBoard
import java.util.*
import kotlin.math.sqrt

class FragmentGameViewModel : ViewModel() {
    private var boardGenerated = listOf<Cell>()

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    var scoreLiveData = MutableLiveData<Int>()
    var isTakingNotesLiveData = MutableLiveData<Boolean>()
    var highlightedKeysLiveData = MutableLiveData<Set<Int>>()

    private var selectedRow = -1
    private var selectedColumn = -1
    private var isTakingNotes = false;
    private val size = 9

    private val board: SudokuBoard

    init {
        boardGenerated = List((size*size)) {i -> Cell(i / 9, i % 9, 0) }
        board = SudokuBoard(size, boardGenerated)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedColumn))
        scoreLiveData.postValue(0)
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    fun handleInput(number: Int){
        if (selectedColumn == -1 || selectedRow == -1) return
        val cell = board.getCell(selectedRow, selectedColumn)
        if (cell.isStartingCell) return

        if (isTakingNotes) {
            if (cell.notes.contains(number)) {
                cell.notes.remove(number)
            } else {
                cell.notes.add(number)
            }
        } else {
            cell.value = if (cell.value == number) 0 else number
        }
        cellsLiveData.postValue(board.cells)
    }

    fun updateSelectedCell(row: Int, column: Int){
        val cell = board.getCell(row, column)
        if (!cell.isStartingCell) {
            selectedRow = row
            selectedColumn = column
            selectedCellLiveData.postValue(Pair(row, column))
        }

        if (isTakingNotes) {
            highlightedKeysLiveData.postValue(cell.notes)
        }
    }

    fun changedNoteTakingState() {
        if (selectedColumn == -1 || selectedRow == -1) return
        isTakingNotes = !isTakingNotes
        isTakingNotesLiveData.postValue(isTakingNotes)

        val curNotes = if (isTakingNotes) {
            board.getCell(selectedRow, selectedColumn).notes
        } else {
            setOf<Int>()
        }
        highlightedKeysLiveData.postValue(curNotes)
    }

    fun createBoard(boardValues: List<Int>){
        val randomListValues = (1..9).shuffled().take(9)
        val randomListIds = (1..9).shuffled().take(9)
        var remapValues: MutableMap<Int,Int> = mutableMapOf(1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5,
            6 to 6, 7 to 7, 8 to 8, 9 to 9)

        for (i in randomListIds.indices){
            remapValues[randomListIds[i]] = randomListValues[i]
        }

        boardValues.forEachIndexed { index, i ->
            boardGenerated[index].value = remapValues[i]!!
        }

//        boardGenerated.forEachIndexed{ index, cell ->
//            for (candidateNumber in randomList) {
//                if (validateCellPosition(index, cell, candidateNumber)) {
//                    boardGenerated[index].value = candidateNumber
//                    break
//                }
//            }
//        }
        board.cells = boardGenerated
        cellsLiveData.postValue(board.cells)
    }

//    private fun validateCellPosition(index: Int, cell: Cell, candidateNumber: Int): Boolean {
//        var passed = true
//        val column = cell.column
//        val row = cell.row
//        val sqrtSize = sqrt(size.toDouble()).toInt()
//
//
//        boardGenerated.forEach {
//            // row and column check
//            if (row == it.row || column == it.column){
//                if (candidateNumber == it.value) {
//                    passed = false
//                }
//            }
//            // square check
//            else if (row/sqrtSize == it.row / sqrtSize && column/sqrtSize == it.column/sqrtSize) {
//                if (candidateNumber == it.value) {
//                    passed = false
//                }
//            }
//        }
//        return passed
//    }

    fun incrementScore(){
        scoreLiveData.postValue(scoreLiveData.value?.plus(10))
    }
}