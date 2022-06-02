package com.example.sudoku

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SudokuBoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    private val sqrtSize = 3
    private val size = 9

    private var cellSizePixels = 0F
    private var selectedRow = 8
    private var selectedColumn = 8

    private var listener: OnTouchListener? = null

    private val thickLinePaint = Paint().apply{
        style = Paint.Style.STROKE
        color = Color.WHITE
        strokeWidth = 6F
    }

    private val thinLinePaint = Paint().apply{
        style = Paint.Style.STROKE
        color = Color.WHITE
        strokeWidth = 3F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6EAD3A")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#2d3250")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
    }

    private fun fillCells(canvas: Canvas){
        if(selectedColumn == -1 || selectedRow == -1) return
        for (r in 0..size){
            for(c in 0..size){
                if (r == selectedRow && c == selectedColumn){
                    fillCell(canvas, r, c, selectedCellPaint)
                } else if (r == selectedRow || c == selectedColumn){
                    fillCell(canvas, r, c, conflictingCellPaint)
                } else if (r/sqrtSize == selectedRow / sqrtSize && c/sqrtSize == selectedColumn/sqrtSize) {
                    fillCell(canvas, r, c, conflictingCellPaint)
                }
            }
        }
    }
    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint){
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels, (c+1) * cellSizePixels, (r+1) * cellSizePixels, paint)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until size) {
            val paintToUse = when (i % sqrtSize){
                0 -> thickLinePaint
                else -> thinLinePaint
            }

            canvas.drawLine(i * cellSizePixels,
                0F,
                i * cellSizePixels,
                height.toFloat(),
                paintToUse)

            canvas.drawLine(0F,
                i * cellSizePixels,
                width.toFloat(),
                i * cellSizePixels,
                paintToUse)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedColumn = (x / cellSizePixels).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedColumn)
    }

    fun updateSelectedCellUI(row: Int, column: Int) {
        selectedRow = row
        selectedColumn = column
        invalidate()
    }

    fun registerListener(listener: OnTouchListener){
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, column: Int)
    }
}