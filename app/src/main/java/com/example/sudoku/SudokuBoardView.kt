package com.example.sudoku

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class SudokuBoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    private var cells: List<Cell>? = null
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
        strokeWidth = 4F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6EAD3A")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#2d3250")
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        cells?.forEach {
            val valueString = it.value.toString()

            val textBounds = Rect()
            textPaint.getTextBounds(valueString, 0, valueString.length, textBounds)
            val textWidth = textPaint.measureText(valueString)
            val textHeight = textBounds.height()

            canvas.drawText(valueString, (it.column * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
            (it.row * cellSizePixels) + cellSizePixels / 2 - textHeight / 2, textPaint)
        }
    }

    private fun fillCells(canvas: Canvas){
        if(selectedColumn == -1 || selectedRow == -1) return

        cells?.forEach {
            val r = it.row
            val c = it.column
            if (r == selectedRow && c == selectedColumn){
                fillCell(canvas, r, c, selectedCellPaint)
            } else if (r == selectedRow || c == selectedColumn){
                fillCell(canvas, r, c, conflictingCellPaint)
            } else if (r/sqrtSize == selectedRow / sqrtSize && c/sqrtSize == selectedColumn/sqrtSize) {
                fillCell(canvas, r, c, conflictingCellPaint)
            }

        }
    }
    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint){
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels, (c+1) * cellSizePixels, (r+1) * cellSizePixels, paint)
    }

    private fun drawLines(canvas: Canvas) {
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

        canvas.drawRect(3F, 3F, width.toFloat(), height.toFloat(), thickLinePaint)
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

    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, column: Int)
    }
}