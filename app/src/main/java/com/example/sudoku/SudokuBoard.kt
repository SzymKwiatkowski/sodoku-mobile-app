package com.example.sudoku

class SudokuBoard(val size: Int, val cells: List<Cell>) {

    fun getCell(row: Int, column: Int) = cells[row * size + column]
}

class Cell(
    val row: Int,
    val column: Int,
    var value: Int,
    var isStartingCell: Boolean = false
)