package com.example.gpayintegration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.absoluteValue

/**
 * TODO: document your custom view class.
 */
class Stepper : View {

    private var cellWidth = 0.toFloat()
    private var cellHeight = 24.toFloat()
    private var maxStateNumber = 1
    private var currentStep = 1
    private var stepSpace = 10.toFloat()
    private var radius = 12.toFloat()
    private var stepType = StepType.GRADIENT

    private var checkedColor1: Int = Color.GREEN
    private var checkedColor2: Int = Color.RED

    private var unCheckedColor1 = Color.LTGRAY
    private var unCheckedColor2 = Color.LTGRAY

    private val checkedPaint = Paint()
    private val uncheckedPaint = Paint()
    private val path = Path()


    private lateinit var firstCorner: FloatArray
    private lateinit var lastCorner: FloatArray
    private lateinit var center: FloatArray


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.Stepper, defStyle, 0
        )

        cellHeight = a.getDimension(R.styleable.Stepper_stepHeight, cellHeight)
        stepSpace = a.getDimension(R.styleable.Stepper_stepSpace, stepSpace)
        radius = a.getDimension(R.styleable.Stepper_stepCornerRadius, radius)
        stepType = StepType.values()[a.getInt(R.styleable.Stepper_stepType, stepType.ordinal)]
        maxStateNumber = a.getInt(R.styleable.Stepper_stepCount, maxStateNumber)
        currentStep = a.getInt(R.styleable.Stepper_stepSelected, currentStep).absoluteValue.takeIf { it<=maxStateNumber}?: maxStateNumber

        checkedColor1 = a.getColor(R.styleable.Stepper_stepCheckedColor, checkedColor1)
        checkedColor2 = a.getColor(R.styleable.Stepper_stepCheckedColor2, checkedColor2)
        unCheckedColor1 = a.getColor(R.styleable.Stepper_stepUnCheckedColor, unCheckedColor1)
        unCheckedColor1 = a.getColor(R.styleable.Stepper_stepUnCheckedColor2, unCheckedColor1)

        setupCorners()
        a.recycle()

    }

    private fun setupCorners() {
        firstCorner = floatArrayOf(
            radius, radius,
            0f, 0f,
            0f, 0f,
            radius, radius
        )

        lastCorner = floatArrayOf(
            0f, 0f,
            radius, radius,
            radius, radius,
            0f, 0f
        )

        center = floatArrayOf(
            0f, 0f,
            0f, 0f,
            0f, 0f,
            0f, 0f
        )
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (index in 0 until maxStateNumber) {

            val stepItem = getStepItem(index)

            val paint = if(stepItem.checked) checkedPaint else uncheckedPaint
            paint.isDither = true
            paint.shader = stepItem.gd
            path.reset()
            when (index) {
                0 -> {
                    path.addRoundRect(
                        stepItem.rect,
                        firstCorner,
                        Path.Direction.CW
                    )
                }
                maxStateNumber -1 -> {
                    path.addRoundRect(
                        stepItem.rect,
                        lastCorner,
                        Path.Direction.CW
                    )
                }
                else -> {
                    path.addRoundRect(
                        stepItem.rect,
                        center,
                        Path.Direction.CW
                    )
                }
            }
            canvas.drawPath(path, paint)
        }


    }

    private fun getStepItem(index: Int): StepItem {
        val startX = (cellWidth * index) + (stepSpace * index)
        val endX = startX + cellWidth
        val startY = 0.toFloat()
        val endY = cellHeight
        val rect = RectF(
            startX,
            startY,
            endX,
            endY
        )
        val isChecked = index < currentStep
        val gradient = if (isChecked) LinearGradient(startX, startY, endX, endY, checkedColor1, checkedColor2, android.graphics.Shader.TileMode.CLAMP)
        else LinearGradient(startX, startY, endX, endY, unCheckedColor1, unCheckedColor2, android.graphics.Shader.TileMode.CLAMP)
        return StepItem().apply {
            this.gd = gradient
            this.rect = rect
            this.checked = isChecked
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, cellHeight.toInt(), oldw, oldh)
        cellWidth = (width - (stepSpace * maxStateNumber-1))/maxStateNumber
    }

    enum class StepType {
        GRADIENT,
        NORMAL
    }
}