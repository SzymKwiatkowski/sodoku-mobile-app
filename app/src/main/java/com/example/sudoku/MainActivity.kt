package com.example.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnExit: Button = findViewById(R.id.btnExit)
        btnExit.setOnClickListener {
            exitProcess(0)
        }

        val btnHome: Button = findViewById(R.id.btnHome)
        btnHome.setOnClickListener {
            findNavController(R.id.fragmentCV).navigate(R.id.action_global_fragmentEntry) }
    }
}