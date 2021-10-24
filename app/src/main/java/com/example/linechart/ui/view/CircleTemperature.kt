package com.example.linechart.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.linechart.R
import kotlin.math.*

class CircleTemperature : View, ValueAnimator.AnimatorUpdateListener {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
        getValueData()
    }

    private fun getValueData() {
        val stepData = (maxValue - minValue).toFloat() / DISPLAY_LEVEL
        for (i in 0..DISPLAY_LEVEL) {
            valueData.add(getStringValue(-(minValue + (i * stepData))))
        }
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTemperatureView)
        radius = typedArray.getInt(R.styleable.CircleTemperatureView_radius, DEFAULT_RADIUS)
        minValue = typedArray.getInt(R.styleable.CircleTemperatureView_min, DEFAULT_MIN_VALUE)
        maxValue = typedArray.getInt(R.styleable.CircleTemperatureView_max, DEFAULT_MAX_VALUE)
        currentValue = minValue.toFloat()
        typedArray.recycle()
    }

    private val gradientColors = intArrayOf(
        ContextCompat.getColor(context, R.color.gradient_start),
        ContextCompat.getColor(context, R.color.gradient_end),
    )
    private var rectF = RectF()
    private var minValue: Int = DEFAULT_MIN_VALUE
    private var maxValue: Int = DEFAULT_MAX_VALUE
    private var radius: Int = DEFAULT_RADIUS
    private var marginContent: Float = MARGIN_CONTENT
    private var spaceTextAndLine: Float = SPACE_TEXT_AND_LINE
    private var spaceLineAndProgress: Float = SPACE_LINE_AND_PROGRESS
    private var spaceProgressAndCircle: Float = SPACE_PROGRESS_AND_CIRCLE
    private var lengthLineShort: Float = LENGTH_LINE_SHORT
    private var lengthLineLong: Float = LENGTH_LINE_LONG
    private var textSizeValue: Float = TEXT_SIZE
    private var textSizeValueCenter: Float = TEXT_SIZE_CENTER
    private var radiusProgress = 0F
    private var radiusLine = 0F
    private var radiusText = 0F
    private val valueData = mutableListOf<String>()
    private val totalValue = maxValue - minValue
    private val stepValue = totalValue / DISPLAY_LEVEL
    private val stepDegreeText =
        (DEGREE_END_DRAW_LINE - DEGREE_START_DRAW_LINE) * stepValue / totalValue
    private val stepDegreeLine = stepDegreeText / 5

    private val boundsTextValue = Rect()
    private val boundsTextValueCenter = Rect()
    private var centerPoint = PointF()
    private val paintCircle: Paint = Paint()
    private val paintProgress = Paint()
    private val paintLine = Paint()
    private val paintText = Paint()

    private fun initPain() {
        paintCircle.apply {
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            style = Paint.Style.FILL
            maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.INNER)
        }

        paintProgress.apply {
            strokeWidth = radius * STROKE_WIDTH_PAIN_PROGRESS
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.background_progress)
        }

        paintLine.apply {
            strokeWidth = radius * STROKE_WIDTH_PAIN_LINE
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.background_line)
        }

        paintText.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    private var currentValue = minValue.toFloat()
    fun setValue(value: Float) {
        if (value > minValue && value <= maxValue) {
            this.currentValue = value
            startAnimation()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        getCenterPoint(w, h)
        marginContent = radius * MARGIN_CONTENT
        spaceTextAndLine = radius * SPACE_TEXT_AND_LINE
        spaceLineAndProgress = radius * SPACE_LINE_AND_PROGRESS
        spaceProgressAndCircle = radius * SPACE_PROGRESS_AND_CIRCLE
        lengthLineShort = radius * LENGTH_LINE_SHORT
        lengthLineLong = radius * LENGTH_LINE_LONG
        radiusProgress = radius + spaceProgressAndCircle
        radiusLine = radiusProgress + spaceLineAndProgress
        radiusText = radiusLine + lengthLineLong + spaceTextAndLine
        textSizeValue = radius * TEXT_SIZE
        textSizeValueCenter = radius * TEXT_SIZE_CENTER

        rectF = RectF(
            centerPoint.x - radiusProgress,
            centerPoint.y - radiusProgress,
            centerPoint.x + radiusProgress,
            centerPoint.y + radiusProgress
        )
        initPain()
    }

    private fun getCenterPoint(w: Int, h: Int) {
        centerPoint.x = w / 2F
        centerPoint.y = h / 2F
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCircle(canvas)
        drawTextCenter(canvas)

        drawTextLevel(canvas)
        drawLineFull(canvas)
        drawProgressBorder(canvas)
        drawLineValue(canvas)
        drawProgressValue(canvas)
    }

    private fun drawProgressBorder(canvas: Canvas?) = drawProgress(canvas, true)
    private fun drawProgressValue(canvas: Canvas?) = drawProgress(canvas, false)

    private fun drawLineFull(canvas: Canvas?) = drawLine(canvas, true)
    private fun drawLineValue(canvas: Canvas?) = drawLine(canvas, false)


    private fun drawCircle(canvas: Canvas?) {
        paintCircle.color = ContextCompat.getColor(context, R.color.grey)
        paintCircle.shader = null
        canvas?.drawCircle(centerPoint.x, centerPoint.y, radius.toFloat(), paintCircle)

        paintCircle.shader = LinearGradient(
            centerPoint.x,
            centerPoint.y - radius,
            centerPoint.x,
            centerPoint.y + radius,
            gradientColors,
            null,
            Shader.TileMode.CLAMP
        )
        canvas?.drawCircle(centerPoint.x, centerPoint.y, radius.toFloat(), paintCircle)
    }

    private fun drawTextCenter(canvas: Canvas?) {
        paintText.apply {
            reset()
            isFakeBoldText = true
            textSize = textSizeValueCenter
            color = ContextCompat.getColor(context, R.color.white)
        }
        val valueText = getStringValue(currentValue)
        paintText.getTextBounds(valueText, 0, valueText.length, boundsTextValueCenter)
        val xPosition = centerPoint.x - boundsTextValueCenter.width() / 2
        val yPosition = centerPoint.y + boundsTextValueCenter.height() / 2
        canvas?.drawText(valueText, xPosition, yPosition, paintText)
    }

    private fun drawTextLevel(canvas: Canvas?) {
        paintText.apply {
            reset()
            textSize = textSizeValue
            color = ContextCompat.getColor(context, R.color.black)
        }
        for ((index, degree) in (DEGREE_START_DRAW_LINE..DEGREE_END_DRAW_LINE step stepDegreeText).withIndex()) {
            val xValue = sin(degreeToRadian(degree)).toFloat()
            val yValue = cos(degreeToRadian(degree)).toFloat()
            paintText.getTextBounds(valueData[index], 0, valueData[index].length, boundsTextValue)
            val xPosition =
                centerPoint.x + (xValue * radiusText) - boundsTextValue.width() / 2
            val yPosition =
                centerPoint.y + (yValue * radiusText) + boundsTextValue.height() / 2
            canvas?.drawText(valueData[index], xPosition, yPosition, paintText)
        }
    }

    private fun getStringValue(value: Float): String {
        return if (value == value.toInt().toFloat()) resources.getString(
            R.string.tempInt,
            value.toInt()
        )
        else resources.getString(
            R.string.tempFloat,
            value
        )
    }

    private fun drawProgress(canvas: Canvas?, isDrawBackground: Boolean) {
        val startAngle = START_ANGLE
        var sweepAngle = TOTAL_SWIPE
        if (isDrawBackground) {
            paintProgress.apply {
                color = ContextCompat.getColor(context, R.color.background_progress)
                shader = null
            }
        } else {

            paintProgress.apply {
                shader = LinearGradient(
                    centerPoint.x - radiusProgress,
                    centerPoint.y,
                    centerPoint.x + radiusProgress,
                    centerPoint.y,
                    gradientColors,
                    null,
                    Shader.TileMode.CLAMP
                )
                color = ContextCompat.getColor(context, R.color.green)
            }
            sweepAngle = (currentValue - minValue) * SWEEP_ANGLE
            if (sweepAngle == 0F) sweepAngle = 0.5F
        }
        canvas?.drawArc(rectF, startAngle, sweepAngle, false, paintProgress)
    }

    private fun drawLine(canvas: Canvas?, isDrawBackground: Boolean) {
        val startDegree = DEGREE_START_DRAW_LINE
        var endDegree = DEGREE_END_DRAW_LINE
        if (isDrawBackground) {
            paintLine.apply {
                color = ContextCompat.getColor(context, R.color.background_line)
                shader = null
            }
        } else {
            paintLine.apply {
                color = ContextCompat.getColor(context, R.color.green)
                shader = LinearGradient(
                    centerPoint.x - radiusLine,
                    centerPoint.y,
                    centerPoint.x + radiusLine,
                    centerPoint.y,
                    gradientColors,
                    null,
                    Shader.TileMode.CLAMP
                )
            }
            endDegree = ((currentValue.toInt() - minValue) * stepDegreeLine / 2) + startDegree
        }

        for (degree in startDegree..endDegree step stepDegreeLine) {
            val xValue = sin(degreeToRadian(degree)).toFloat()
            val yValue = cos(degreeToRadian(degree)).toFloat()

            val startX = centerPoint.x - xValue * radiusLine
            val startY = centerPoint.y + yValue * radiusLine
            var endX: Float
            var endY: Float

            if (degree % DEGREE_LONG_LINE == 0) {
                endX =
                    centerPoint.x - xValue * (radiusLine + lengthLineLong)
                endY = centerPoint.y + yValue * (radiusLine + lengthLineLong)
            } else {
                endX = centerPoint.x - xValue * (radiusLine + lengthLineShort)
                endY = centerPoint.y + yValue * (radiusLine + lengthLineShort)
            }
            canvas?.drawLine(startX, startY, endX, endY, paintLine)
        }
    }

    private fun startAnimation() {
        val animator: ValueAnimator = ValueAnimator.ofInt(minValue, currentValue.toInt())
        animator.duration = DELAY
        animator.addUpdateListener(this)
        animator.start()
    }

    override fun onAnimationUpdate(p0: ValueAnimator?) {
        val valueTemp = p0?.animatedValue as? Int
        if (valueTemp != null) {
            currentValue = valueTemp.toFloat()
        }
        invalidate()
    }

    private fun degreeToRadian(degree: Int) = degree * PI / 180

    companion object {
        private const val DISPLAY_LEVEL = 4
        private const val DEFAULT_MIN_VALUE = -20
        private const val DEFAULT_MAX_VALUE = 20
        private const val DEFAULT_RADIUS = 250

        private const val SPACE_TEXT_AND_LINE = 0.25F
        private const val SPACE_LINE_AND_PROGRESS = 0.15F
        private const val SPACE_PROGRESS_AND_CIRCLE = 0.15F

        private const val LENGTH_LINE_SHORT = 0.15F
        private const val LENGTH_LINE_LONG = 0.25F

        private const val TEXT_SIZE = 0.2F
        private const val TEXT_SIZE_CENTER = 0.35F

        private const val DEGREE_LONG_LINE = 240 / DISPLAY_LEVEL
        private const val DEGREE_START_DRAW_LINE = 60
        private const val DEGREE_END_DRAW_LINE = 300

        private const val MARGIN_CONTENT = 0.1F

        private const val TOTAL_SWIPE = 240F
        private const val START_ANGLE = 150F
        private const val SWEEP_ANGLE = TOTAL_SWIPE / 40

        private const val STROKE_WIDTH_PAIN_LINE = 0.03F
        private const val STROKE_WIDTH_PAIN_PROGRESS = 0.15F

        private const val DELAY = 1000L
    }
}