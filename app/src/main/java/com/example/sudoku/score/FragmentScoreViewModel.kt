package com.example.sudoku.score

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentScoreViewModel : ViewModel() {
    var score = MutableLiveData<Int>()

}