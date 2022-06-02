package com.example.sudoku

import androidx.lifecycle.MutableLiveData

class SudokuGame {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()

    private var selectedRow = -1
    private var selectedColumn = -1

    init {
        selectedCellLiveData.postValue(Pair(selectedRow, selectedColumn))
    }

    fun updateSelectedCell(row: Int, column: Int){
        selectedRow = row
        selectedColumn = column
        selectedCellLiveData.postValue(Pair(row, column))
    }
}