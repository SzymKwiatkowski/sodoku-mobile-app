package com.example.sudoku

class SudokuBoard(val size: Int, var cells: List<Cell>) {

    fun getCell(row: Int, column: Int) = cells[row * size + column]
}

class Cell(
    val row: Int,
    val column: Int,
    var value: Int,
    var isLockedCell: Boolean = false,
    var wrongCell: Boolean = false,
    var notes: MutableSet<Int> = mutableSetOf()
)