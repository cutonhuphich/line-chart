package com.example.linechart.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.linechart.R
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

    private var center = PointF()

    private val paintCircle: Paint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        color = ContextCompat.getColor(context, R.color.black)
        style = Paint.Style.FILL
        strokeWidth = 5f
    }

    private val paintBorder = Paint().apply {
        strokeWidth = 50f
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.black)
    }

    private val paintLine = Paint().apply {
        strokeWidth = 10f
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.black)
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

    private fun calculateRadius(d: Int): Float = d / 2F

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center.x = w / 2F
        center.y = h / 2F
    }

    private fun drawCircle(canvas: Canvas?) {
        canvas?.drawCircle(center.x, center.y, getRadiusCircle(), paintCircle)
    }

    private fun drawBorder(canvas: Canvas?) {
        val rectF = RectF(
            center.x - getRadiusBorder(),
            center.y - getRadiusBorder(),
            center.x + getRadiusBorder(),
            center.y + getRadiusBorder()
        )
        canvas?.drawArc(rectF, START_ANGLE, SWEEP_ANGLE, false, paintBorder)
    }

    private fun drawLine(canvas: Canvas?) {
        for (degree in DEGREE_START_DRAW_LINE..DEGREE_END_DRAW_LINE step 10) {
            val xValue = sin(degreeToRadian(degree)).toFloat()
            val yValue = cos(degreeToRadian(degree)).toFloat()

            val startX = center.x + xValue * getRadiusLine()
            val startY = center.y + yValue * getRadiusLine()
            val endX = center.x + xValue * (getRadiusLine() + getLengthLine(degree))
            val endY = center.y + yValue * (getRadiusLine() + getLengthLine(degree))

            canvas?.drawLine(startX, startY, endX, endY, paintLine)
        }
    }

    private fun getLengthLine(degree: Int): Float =
        if (degree % 60 == 0) LENGTH_LINE_LONG else LENGTH_LINE_SHORT

    private fun degreeToRadian(degree: Int) = degree * PI / 180

    companion object {
        private const val LENGTH_LINE_SHORT = 50F
        private const val LENGTH_LINE_LONG = 80F
        private const val DEGREE_START_DRAW_LINE = 60
        private const val DEGREE_END_DRAW_LINE = 300
        private const val MARGIN_CONTENT = 50F

        private const val START_ANGLE = 150F
        private const val SWEEP_ANGLE = 240F
    }
}