package com.example.linechart.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.linechart.R

class LineChart : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initData(attrs)
    }

    private var minValue = DEFAULT_MIN_VALUE
    private var maxValue = DEFAULT_MAX_VALUE
    private var marginLeft = MARGIN_LEFT_PARENT
    private var marginRight = MARGIN_RIGHT_PARENT
    private var marginTop = MARGIN_TOP_PARENT
    private var marginBottom = MARGIN_BOTTOM_PARENT
    private var lineNumber = 0
    private var spaceBetweenLines = 0F
    private var spaceVertical = 0F
    private var bottomPosition = 0F
    private var heightContent = 0F
    private var widthContent = 0F

    private val path = Path()
    private val paintHorizontalLine = Paint()
    private val paintText = Paint()
    private val paintLine = Paint()

    private val controlPoint1 = arrayListOf<PointF>()
    private val controlPoint2 = arrayListOf<PointF>()
    private val pointS = arrayListOf<PointF>()
    private var valueData = floatArrayOf()
    fun setData(values: FloatArray) {
        valueData = values

    }

    @SuppressLint("CustomViewStyleable")
    private fun initData(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChartView)
        maxValue = typedArray.getInt(R.styleable.LineChartView_lineMaxValue, DEFAULT_MAX_VALUE)
        minValue = typedArray.getInt(R.styleable.LineChartView_lineMinValue, DEFAULT_MIN_VALUE)
        lineNumber = 1 + (maxValue - minValue) / 10
        typedArray.recycle()
    }

    private fun calculateControlPoint() {
        for (i in 1 until pointS.size) {
            controlPoint1.add(PointF((pointS[i].x + pointS[i - 1].x) / 2, pointS[i - 1].y))
            controlPoint2.add(PointF((pointS[i].x + pointS[i - 1].x) / 2, pointS[i].y))
        }
    }

    private fun calculatePointS() {
        valueData.forEachIndexed { index, value ->
            val xPosition = (index * spaceVertical) + marginLeft
            val yPosition = bottomPosition - ((-minValue + value) * spaceBetweenLines / 10)
            pointS.add(PointF(xPosition, yPosition))
        }
    }


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
            color = ContextCompat.getColor(context, R.color.blue)
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        marginLeft = w * MARGIN_LEFT_PARENT
        marginRight = w * MARGIN_RIGHT_PARENT
        marginTop = h * MARGIN_TOP_PARENT
        marginBottom = h * MARGIN_BOTTOM_PARENT
        bottomPosition = h - marginBottom
        heightContent = h - marginBottom - marginTop
        widthContent = w - marginLeft - marginRight
        spaceBetweenLines = heightContent / (lineNumber - 1)
        spaceVertical = widthContent / (valueData.size - 1)

        initPain()
        calculatePointS()
        calculateControlPoint()
    }

    private fun drawHorizontalLine(canvas: Canvas?) {
        path.reset()
        path.fillType = Path.FillType.EVEN_ODD
        for (i in 0 until lineNumber) {
            path.moveTo(marginLeft, marginTop + (i * spaceBetweenLines))
            path.lineTo(marginLeft + widthContent, marginTop + (i * spaceBetweenLines))
            path.close()
            canvas?.drawPath(path, paintHorizontalLine)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawHorizontalLine(canvas)
        drawPoint(canvas)
    }

    private fun drawPoint(canvas: Canvas?) {
        if (pointS.isEmpty() && controlPoint1.isEmpty() && controlPoint2.isEmpty()) return
        path.reset()
        path.moveTo(pointS.first().x, pointS.first().y)

        for (i in 1 until pointS.size) {
            path.cubicTo(
                controlPoint1[i - 1].x,
                controlPoint1[i - 1].y,
                controlPoint2[i - 1].x,
                controlPoint2[i - 1].y,
                pointS[i].x,
                pointS[i].y
            )
        }

        path.set(path)
        canvas?.drawPath(path, paintLine)
    }

    companion object {
        private const val DEFAULT_MAX_VALUE = 10
        private const val DEFAULT_MIN_VALUE = -30


        private const val MARGIN_LEFT_PARENT = 0.08f
        private const val MARGIN_TOP_PARENT = 0.08f
        private const val MARGIN_RIGHT_PARENT = 0.08f
        private const val MARGIN_BOTTOM_PARENT = 0.08f
    }
}