package com.example.linechart.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.linechart.R

class LineChart : View {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    private val minValue = -30
    private val maxValue = 10
    private val path = Path()

    private val paintHorizontalLine: Paint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        color = ContextCompat.getColor(context, R.color.grey)
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 5f
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.grey)
        style = Paint.Style.FILL
        textSize = 36F
    }

    private val paintValue: Paint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        color = ContextCompat.getColor(context, R.color.grey)
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 5f
    }

    private fun getMarginLeft() = width * MARGIN_LEFT_PARENT

    private fun getMarginRight() = width * MARGIN_RIGHT_PARENT

    private fun getMarginTop() = height * MARGIN_TOP_PARENT

    private fun getMarginBottom() = height * MARGIN_BOTTOM_PARENT

    private fun getHeightContent() = height - getMarginTop() - getMarginBottom()

    private fun getWidthContent() = width - getMarginLeft() - getMarginRight()

    private fun getMarginLineVertical() = getHeightContent() / (maxValue - minValue)


    private fun drawHorizontalLine(canvas: Canvas?) {
        path.reset()
        path.fillType = Path.FillType.EVEN_ODD
        for (i in 0..5) {
            path.moveTo(0f + getMarginLeft(), getMarginTop() + (i * getMarginLineVertical()))
            path.lineTo(width - getMarginLeft(), getMarginTop() + (i * getMarginLineVertical()))
            path.close()
            canvas?.drawPath(path, paintHorizontalLine)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        path.reset()
        drawHorizontalLine(canvas)
    }

    private fun drawXAxisValue(canvas: Canvas?) {
//        val yAxisValue = (0 until (_maxValue.toInt() / 5) + 1).map { "${5 * it}" }
//
//        yAxisValue.forEachIndexed { index, s ->
//            mPaintText.getTextBounds(s, 0, s.length, bounds)
//            val yPosition = getBottomPosition() - index * getHeightContent() / (yAxisValue.size - 1) + bounds.height()
//            val xPosition = getMarginLeft() - bounds.width() - getMarginXAxisText()
//            canvas?.drawText(s, xPosition, yPosition, mPaintText)
//        }
    }

    companion object {
        private const val MARGIN_LEFT_PARENT = 0.08f
        private const val MARGIN_TOP_PARENT = 0.08f
        private const val MARGIN_RIGHT_PARENT = 0.08f
        private const val MARGIN_BOTTOM_PARENT = 0.08f

        private const val SIZE_TEXT = 36F
        private const val MARGIN_TEXT = 0.03f
        private const val MARGIN_CONTENT = 0.05f
        private const val WIDTH_COLUMN = 100f
    }
}