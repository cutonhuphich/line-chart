package com.example.linechart.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.linechart.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

class CircleTemperature : View {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    private val paintCircle: Paint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        color = ContextCompat.getColor(context, R.color.black)
        style = Paint.Style.STROKE
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
        drawCircle(canvas)
        drawBorder(canvas)
        drawLine(canvas)
    }


    private fun getRadiusCircle() =
        if (width > height) calculateRadius(height) else calculateRadius(width)

    private fun getRadiusBorder() = getRadiusCircle() + 50F
    private fun getRadiusLine() = getRadiusBorder() + 50F

    private fun calculateRadius(d: Int): Float = d / 2F - 200f

    private var center = PointF()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center.x = w / 2f
        center.y = h / 2f
    }

    private fun drawCircle(canvas: Canvas?) {
        canvas?.drawCircle(center.x, center.y, getRadiusCircle(), paintCircle)
    }

    private fun drawBorder(canvas: Canvas?) {

        canvas?.drawPoint(100f, 100f, paintCircle)
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

            val startX = center.x + (sin(degree * PI / 180) * getRadiusLine()).toFloat()
            val startY = center.y + (cos(degree * PI / 180) * getRadiusLine()).toFloat()

            val endX =
                center.x + (sin(degree * PI / 180) * (getRadiusLine() + LENGTH_LINE_SHORT)).toFloat()
            val endY =
                center.y + (cos(degree * PI / 180) * (getRadiusLine() + LENGTH_LINE_SHORT)).toFloat()
            canvas?.drawLine(startX, startY, endX, endY, paintLine)

        }
    }

    companion object {
        private const val LENGTH_LINE_SHORT = 50F
        private const val LENGTH_LINE_LONG = 70F
        private const val DEGREE_START_DRAW_LINE = 45
        private const val DEGREE_END_DRAW_LINE = 315

        private const val START_ANGLE = 150F
        private const val SWEEP_ANGLE = 240F
    }
}