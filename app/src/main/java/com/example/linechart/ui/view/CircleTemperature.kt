package com.example.linechart.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.linechart.R
import java.nio.channels.FileLock
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CircleTemperature : View {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var minValue = -20F
    private fun setMinValue(value: Float) {
        this.minValue = value
    }

    private var maxValue = 20F
    private fun setMaxValue(value: Float) {
        this.maxValue = value
    }

    private var currentValue = minValue
    private fun setValue(value: Float) {
        this.currentValue = value
    }

    private val bounds = Rect()
    private var centerPoint = PointF()

    private val paintCircle: Paint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        color = ContextCompat.getColor(context, R.color.green)
        style = Paint.Style.FILL
    }

    private val paintBorder = Paint().apply {
        strokeWidth = STROKE_WIDTH_PAIN_BORDER
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.green)
    }

    private val paintLine = Paint().apply {
        strokeWidth = STROKE_WIDTH_PAIN_LINE
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.green)
    }


    private val paintText = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.black)
        style = Paint.Style.FILL
        textSize = TEXT_SIZE
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLine(canvas)
        drawBorder(canvas)
        drawCircle(canvas)
    }

    private fun getRadius() =
        if (width > height) calculateRadius(height) else calculateRadius(width)

    private fun getRadiusLine() = getRadius() - MARGIN_CONTENT - LENGTH_LINE_LONG
    private fun getRadiusBorder() = getRadiusLine() - MARGIN_CONTENT
    private fun getRadiusCircle() = getRadiusBorder() - MARGIN_CONTENT

    private fun calculateRadius(d: Int) = d / 2F

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerPoint.x = w / 2F
        centerPoint.y = h / 2F
    }

    private fun drawCircle(canvas: Canvas?) {
        canvas?.drawCircle(centerPoint.x, centerPoint.y, getRadiusCircle(), paintCircle)
    }

    private fun drawBorder(canvas: Canvas?) {
        val rectF = RectF(
            centerPoint.x - getRadiusBorder(),
            centerPoint.y - getRadiusBorder(),
            centerPoint.x + getRadiusBorder(),
            centerPoint.y + getRadiusBorder()
        )
        canvas?.drawArc(rectF, START_ANGLE, SWEEP_ANGLE, false, paintBorder)
    }

    private fun drawLine(canvas: Canvas?) {
        var textValue = minValue
        for (degree in DEGREE_START_DRAW_LINE..DEGREE_END_DRAW_LINE step getStepDegree().toInt()) {
            val xValue = sin(degreeToRadian(degree)).toFloat()
            val yValue = cos(degreeToRadian(degree)).toFloat()


            val startX = centerPoint.x + xValue * getRadiusLine()
            val startY = centerPoint.y + yValue * getRadiusLine()
            var endX: Float
            var endY: Float

            if (degree % 60 == 0) {

                endX = centerPoint.x + xValue * (getRadiusLine() + LENGTH_LINE_LONG)
                endY = centerPoint.y + yValue * (getRadiusLine() + LENGTH_LINE_LONG)

                paintText.getTextBounds(
                    textValue.toString(),
                    0,
                    textValue.toString().length,
                    bounds
                )

                //Draw text Value
                val xPosition =
                    centerPoint.x + (xValue * (getRadiusLine() + LENGTH_LINE_LONG + 50f))+(xValue * bounds.width() / 2)
                val yPosition =
                    centerPoint.y + yValue * (getRadiusLine() + LENGTH_LINE_LONG + 50f)
                canvas?.drawText(textValue.toString(), xPosition, yPosition, paintText)
                textValue += getStepValue()

            } else {
                endX = centerPoint.x + xValue * (getRadiusLine() + LENGTH_LINE_SHORT)
                endY = centerPoint.y + yValue * (getRadiusLine() + LENGTH_LINE_SHORT)
            }
            canvas?.drawLine(startX, startY, endX, endY, paintLine)
        }
    }

    private fun getSumValue() = maxValue - minValue

    private fun getStepValue() = getSumValue() / DISPLAY_LEVEL
    private fun getStepDegree() =
        (DEGREE_END_DRAW_LINE - DEGREE_START_DRAW_LINE) * getStepValue() / (getSumValue() * 5)

    private fun calculateValue(stepValue: Float): List<Float> {
        val values = mutableListOf<Float>()
        for (i in 0 until DISPLAY_LEVEL) {
            values.add(minValue + stepValue)
        }
        return values
    }

    private fun degreeToRadian(degree: Int) = degree * PI / 180

    companion object {
        private const val LENGTH_LINE_SHORT = 50F
        private const val LENGTH_LINE_LONG = 80F
        private const val DEGREE_START_DRAW_LINE = 60
        private const val DEGREE_END_DRAW_LINE = 300
        private const val MARGIN_CONTENT = 50F

        private const val START_ANGLE = 150F
        private const val SWEEP_ANGLE = 240F

        private const val STROKE_WIDTH_PAIN_LINE = 10F
        private const val STROKE_WIDTH_PAIN_BORDER = 40F

        private const val TEXT_SIZE = 30F

        private const val DISPLAY_LEVEL = 4
    }
}