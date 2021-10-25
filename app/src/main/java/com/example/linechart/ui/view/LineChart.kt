package com.example.linechart.ui.view

import android.annotation.SuppressLint
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
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
        //   getValueData()
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChartView)
        minValue = typedArray.getInt(R.styleable.LineChartView_lineMinValue, DEFAULT_MIN_VALUE)
        maxValue = typedArray.getInt(R.styleable.LineChartView_lineMaxValue, DEFAULT_MAX_VALUE)
        typedArray.recycle()
    }

    private var minValue = DEFAULT_MIN_VALUE
    private var maxValue = DEFAULT_MAX_VALUE


    private val path = Path()
    private val paintHorizontalLine = Paint()
    private val paintText = Paint()
    private val paintLine = Paint()

    private fun initPain() {
        paintHorizontalLine.apply {
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            color = ContextCompat.getColor(context, R.color.grey)
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 5f
        }

        paintText.apply {
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.grey)
            style = Paint.Style.FILL
            textSize = 36F
        }

        paintLine.apply {
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            color = ContextCompat.getColor(context, R.color.grey)
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 5f
        }
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
        // drawHorizontalLine(canvas)
    }

    private fun drawXAxisValue(canvas: Canvas?) {

    }//        val yAxisValue = (0 until (_maxValue.toInt() / 5) + 1).map { "${5 * it}" }
//
//        yAxisValue.forEachIndexed { index, s ->
//            mPaintText.getTextBounds(s, 0, s.length, bounds)
//            val yPosition = getBottomPosition() - index * getHeightContent() / (yAxisValue.size - 1) + bounds.height()
//            val xPosition = getMarginLeft() - bounds.width() - getMarginXAxisText()
//            canvas?.drawText(s, xPosition, yPosition, mPaintText)
//        }

    companion object {
        private const val DEFAULT_MIN_VALUE = -30
        private const val DEFAULT_MAX_VALUE = 10


        private const val MARGIN_LEFT_PARENT = 0.08f
        private const val MARGIN_TOP_PARENT = 0.08f
        private const val MARGIN_RIGHT_PARENT = 0.08f
        private const val MARGIN_BOTTOM_PARENT = 0.08f
    }
}