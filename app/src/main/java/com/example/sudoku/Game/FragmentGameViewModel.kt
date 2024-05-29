package com.example.sudoku.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku.Cell
import com.example.sudoku.SudokuBoard
import java.util.*
import kotlin.math.sqrt

enum class Difficulty {
    EASY, MEDIUM, HARD
}

class FragmentGameViewModel : ViewModel() {
    private var boardGenerated = listOf<Cell>()
    private var boardCellsValues = mutableListOf<Int>()

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    var scoreLiveData = MutableLiveData<Int>()
    var isTakingNotesLiveData = MutableLiveData<Boolean>()
    var highlightedKeysLiveData = MutableLiveData<Set<Int>>()
    var progressLiveData = MutableLiveData<Float>()
    var gameEndLiveData = MutableLiveData<Boolean>()
    var textClockLiveData = MutableLiveData<String>()
    private var guessedNumber: Int = 0

    private var selectedRow = -1
    private var selectedColumn = -1
    private var isTakingNotes = false;
    private val size = 9
    private var selectedDifficulty: Difficulty? = null

    private var difficultyTranslator: MutableMap<Difficulty?, Int> = mutableMapOf(Difficulty.EASY to 25,
    Difficulty.MEDIUM to 40, Difficulty.HARD to 54)
//    private var difficultyTranslator: MutableMap<Difficulty?, Int> = mutableMapOf(Difficulty.EASY to 1,
//        Difficulty.MEDIUM to 2, Difficulty.HARD to 3)

    private val board: SudokuBoard

    init {
        boardGenerated = List((size*size)) {i -> Cell(i / 9, i % 9, 0) }
        board = SudokuBoard(size, boardGenerated)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedColumn))
        scoreLiveData.postValue(0)
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
        gameEndLiveData.postValue(false)
    }

    fun handleInput(number: Int){
        if (selectedColumn == -1 || selectedRow == -1) return
        val cell = board.getCell(selectedRow, selectedColumn)
        if (!validateInput(selectedRow * 9 + selectedColumn, number) && !isTakingNotes) return
        if (cell.isLockedCell) return


        if (isTakingNotes) {
            if (cell.wrongCell || cell.value > 0) {
                board.cells[cell.row * 9 + cell.column].value = 0
                board.cells[cell.row * 9 + cell.column].wrongCell = false
            }
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

    private fun validateInput(id: Int, number: Int): Boolean{
        if (isTakingNotes) return true
        return if (boardCellsValues[id] == number) {
            incrementScore()
            board.cells[id].isLockedCell = true
            board.cells[id].value = number
            cellsLiveData.postValue(board.cells)
            true
        } else {
            board.cells[id].wrongCell = true
            board.cells[id].value = number
            scoreLiveData.postValue(0)
            cellsLiveData.postValue(board.cells)
            false
        }
    }

    fun updateSelectedCell(row: Int, column: Int){
        val cell = board.getCell(row, column)
        if (!cell.isLockedCell) {
            selectedRow = row
            selectedColumn = column
            selectedCellLiveData.postValue(Pair(row, column))
        } else {
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

    fun createBoard(boardValues: List<Int>, difficulty: Difficulty){
        selectedDifficulty = difficulty
        progressLiveData.postValue(0F)
        boardCellsValues = MutableList((size*size)) {i -> 0}
        val randomListValues = (1..9).shuffled().take(9)
        val randomListIds = (1..9).shuffled().take(9)
        var remapValues: MutableMap<Int,Int> = mutableMapOf(1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5,
            6 to 6, 7 to 7, 8 to 8, 9 to 9)
        // Create random exchange of numbers
        for (i in randomListIds.indices){
            remapValues[randomListIds[i]] = randomListValues[i]
        }
        // Remap with mapped out random values
        boardValues.forEachIndexed { index, i ->
            boardCellsValues[index] = remapValues[i]!!
            boardGenerated[index].value = remapValues[i]!!
        }
        // Write positions into live array object and save them
        val randomPositions = (0 until (size*size)).shuffled().
            take(difficultyTranslator[selectedDifficulty]!!)
        board.cells.forEachIndexed { index, cell ->
            if (randomPositions.contains(index)) {
                board.cells[index].value = 0
            } else {
                board.cells[index].isLockedCell = true
            }
        }

        cellsLiveData.postValue(board.cells)
    }

    private fun incrementScore(){
        val points = (10 + difficultyTranslator[selectedDifficulty]!! * 1) * 3
        scoreLiveData.postValue(scoreLiveData.value?.plus(points))
        guessedNumber++
        progressLiveData.postValue(guessedNumber.toFloat()/(difficultyTranslator[selectedDifficulty]!!))

        if (guessedNumber >= difficultyTranslator[selectedDifficulty]!!) {
            gameEndLiveData.postValue(true)
        }
    }

    fun postTextClock(valueString: String){
        textClockLiveData.postValue(valueString)
    }
}

