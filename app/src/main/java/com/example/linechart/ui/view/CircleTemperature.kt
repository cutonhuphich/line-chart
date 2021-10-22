package com.example.linechart.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.linechart.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CircleTemperature : View, ValueAnimator.AnimatorUpdateListener {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val minValue = -20F
//     fun setMinValue(value: Float) {
//        this.minValue = value
//        invalidate()
//    }

    private val maxValue = 20F
//     fun setMaxValue(value: Float) {
//        this.maxValue = value
//        invalidate()
//    }

    private var currentValue = minValue
    fun setValue(value: Float) {
        if (value > minValue && value <= maxValue) {
            this.currentValue = value
            startAnimation()
        }
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
        drawTextCenter(canvas)
    }

    private fun getRadius() =
        if (width > height) calculateRadius(height) else calculateRadius(width)

    private fun getRadiusLine() = getRadius() - MARGIN_CONTENT - LENGTH_LINE_LONG
    private fun getRadiusBorder() = getRadiusLine() - MARGIN_CIRCLE
    private fun getRadiusCircle() = getRadiusBorder() - MARGIN_CIRCLE

    private fun calculateRadius(d: Int) = d / 2F

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerPoint.x = w / 2F
        centerPoint.y = h / 2F
    }

    private fun drawCircle(canvas: Canvas?) {
        canvas?.drawCircle(centerPoint.x, centerPoint.y, getRadiusCircle(), paintCircle)
    }

    private fun drawTextCenter(canvas: Canvas?) {
        val valueText = resources.getString(R.string.temp, currentValue.toInt())
        paintText.reset()
        paintText.color = ContextCompat.getColor(context, R.color.white)
        paintText.textSize = TEXT_SIZE_CENTER
        paintText.getTextBounds(valueText, 0, valueText.length, bounds)
        val xPosition = centerPoint.x - bounds.width() / 2
        val yPosition = centerPoint.y + bounds.height() / 2
        canvas?.drawText(valueText, xPosition, yPosition, paintText)
    }

    private fun drawBorder(canvas: Canvas?) {
        val rectF = RectF(
            centerPoint.x - getRadiusBorder(),
            centerPoint.y - getRadiusBorder(),
            centerPoint.x + getRadiusBorder(),
            centerPoint.y + getRadiusBorder()
        )
        canvas?.drawArc(rectF, START_ANGLE, sweepAngle, false, paintBorder)
    }

    private fun drawLine(canvas: Canvas?) {
        var value = -minValue
        for (degree in DEGREE_START_DRAW_LINE..DEGREE_END_DRAW_LINE step getStepDegree().toInt()) {
            val xValue = sin(degreeToRadian(degree)).toFloat()
            val yValue = cos(degreeToRadian(degree)).toFloat()


            val startX = centerPoint.x + xValue * getRadiusLine()
            val startY = centerPoint.y + yValue * getRadiusLine()
            var endX: Float
            var endY: Float

            if (degree % 60 == 0) {

                val stringValue = resources.getString(R.string.temp, value.toInt())
                endX = centerPoint.x + xValue * (getRadiusLine() + LENGTH_LINE_LONG)
                endY = centerPoint.y + yValue * (getRadiusLine() + LENGTH_LINE_LONG)


                paintText.reset()
                paintText.textSize = TEXT_SIZE
                paintText.color = ContextCompat.getColor(context, R.color.black)
                paintText.getTextBounds(
                    stringValue,
                    0,
                    stringValue.length,
                    bounds
                )

                //Draw text Value
                val xPosition =
                    centerPoint.x + (xValue * (getRadiusLine() + MARGIN_TEXT)) - (bounds.width() / 2)
                val yPosition =
                    centerPoint.y + (yValue * (getRadiusLine() + MARGIN_TEXT)) + (bounds.height() / 2)
                canvas?.drawText(stringValue, xPosition, yPosition, paintText)
                value -= getStepValue()

            } else {
                endX = centerPoint.x + xValue * (getRadiusLine() + LENGTH_LINE_SHORT)
                endY = centerPoint.y + yValue * (getRadiusLine() + LENGTH_LINE_SHORT)
            }
            canvas?.drawLine(startX, startY, endX, endY, paintLine)
        }
    }

    private var sweepAngle = 0.5F

    private fun startAnimation() {
        val animator: ValueAnimator = ValueAnimator.ofInt(minValue.toInt(), currentValue.toInt())
        animator.duration = DELAY
        animator.addUpdateListener(this)
        animator.start()
    }

    override fun onAnimationUpdate(p0: ValueAnimator?) {
        val valueTemp = p0?.animatedValue as? Int
        if (valueTemp != null) {
            sweepAngle = (valueTemp + 20) * SWEEP_ANGLE
        }
        invalidate()
    }

    private fun getSumValue() = maxValue - minValue

    private fun getStepValue() = getSumValue() / DISPLAY_LEVEL
    private fun getStepDegree() =
        (DEGREE_END_DRAW_LINE - DEGREE_START_DRAW_LINE) * getStepValue() / (getSumValue() * 5)

    private fun degreeToRadian(degree: Int) = degree * PI / 180

    companion object {
        private const val LENGTH_LINE_SHORT = 30F
        private const val LENGTH_LINE_LONG = 50F
        private const val MARGIN_TEXT = LENGTH_LINE_LONG + 50F

        private const val DEGREE_START_DRAW_LINE = 60
        private const val DEGREE_END_DRAW_LINE = 300
        private const val MARGIN_CONTENT = 80F
        private const val MARGIN_CIRCLE = 30F

        private const val START_ANGLE = 150F
        private const val SWEEP_ANGLE = 240F / 40

        private const val STROKE_WIDTH_PAIN_LINE = 8F
        private const val STROKE_WIDTH_PAIN_BORDER = 30F

        private const val TEXT_SIZE = 35F
        private const val TEXT_SIZE_CENTER = 50F

        private const val DISPLAY_LEVEL = 4

        private const val DELAY = 1000L
    }
}