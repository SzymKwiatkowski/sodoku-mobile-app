package com.example.sudoku

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class SudokuBoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    private var cells: List<Cell>? = null
    private val sqrtSize = 3
    private val size = 9

    private var cellSizePixels = 0F
    private var noteSizePixels = 0F
    private var selectedRow = 0
    private var selectedColumn = 0

    private var listener: OnTouchListener? = null

    private val thickLinePaint = Paint().apply{
        style = Paint.Style.STROKE
        color = Color.WHITE
        strokeWidth = 6F
        alpha = 230
    }

    private val thinLinePaint = Paint().apply{
        style = Paint.Style.STROKE
        color = Color.WHITE
        strokeWidth = 3F
        alpha = 100
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#FF03DAC5")
        alpha = 100
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#2d3250")
        alpha = 200
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
        textSize = 50F
        alpha = 200
    }

    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.CYAN
        textSize = 50F
        alpha = 200
    }

    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#3C256F")
        alpha = 200
    }

    private val noteTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.YELLOW//parseColor("#FFBB86FC")
        textSize = 25F
    }

    private val wrongValueTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.RED
        textSize = 50F
        alpha = 150
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = min(widthMeasureSpec, heightMeasureSpec) - 20
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        updateMeasurments(width)

        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    private fun updateMeasurments(width: Int) {
        cellSizePixels = (width / size).toFloat()
        noteSizePixels = cellSizePixels / sqrtSize.toFloat()
        noteTextPaint.textSize = noteSizePixels / 1.5F
        startingCellTextPaint.textSize = cellSizePixels / 1.5F
        textPaint.textSize = cellSizePixels / 1.5F
        wrongValueTextPaint.textSize = cellSizePixels / 1.5F
    }


    private fun drawText(canvas: Canvas) {
        cells?.forEach { cell ->
            val value = cell.value
            val textBounds = Rect()

            if (value == 0) {
                cell.notes.forEach { note ->
                    val valueString = note.toString()
                    val rowInCell = (note-1) / sqrtSize
                    val columnInCell = (note-1) % sqrtSize
                    noteTextPaint.getTextBounds(valueString, 0, valueString.length, textBounds)
                    val textWidth = noteTextPaint.measureText(valueString)
                    val textHeight = textBounds.height()

                    canvas.drawText(
                        valueString,
                        (cell.column * cellSizePixels) + (columnInCell * noteSizePixels) + noteSizePixels / 2 - textWidth / 2f,
                        (cell.row * cellSizePixels) + (rowInCell * noteSizePixels) + noteSizePixels / 2 + textHeight / 2f,
                        noteTextPaint
                    )
                }
            } else {
                val valueString = value.toString()
                val paintToUse = if (cell.isStartingCell) startingCellTextPaint
                    else if (cell.wrongCell) wrongValueTextPaint else textPaint
                paintToUse.getTextBounds(valueString, 0, valueString.length, textBounds)
                val textWidth = paintToUse.measureText(valueString)
                val textHeight = textBounds.height()
                canvas.drawText(valueString,
                    (cell.column * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                    (cell.row * cellSizePixels) + cellSizePixels / 2 + textHeight / 2,
                    paintToUse)
            }
        }
    }

    private fun fillCells(canvas: Canvas){
        if(selectedColumn == -1 || selectedRow == -1) return

        var value = getCellValue(selectedRow, selectedColumn)
        cells?.forEach {
            val r = it.row
            val c = it.column
            if (it.isStartingCell && it.value==value){
                fillCell(canvas, r, c, startingCellPaint)
            } else if (r == selectedRow && c == selectedColumn){
                fillCell(canvas, r, c, selectedCellPaint)
            } else if (r == selectedRow || c == selectedColumn){
                fillCell(canvas, r, c, conflictingCellPaint)
            } else if (r/sqrtSize == selectedRow / sqrtSize && c/sqrtSize == selectedColumn/sqrtSize) {
                fillCell(canvas, r, c, conflictingCellPaint)
            }
        }
    }

    private fun getCellValue(r: Int, c :Int): Int{
        var value = 0
        cells?.forEach {
            if (it.row == r && it.column == c) {
                value = it.value
            }
        }
        return value
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

