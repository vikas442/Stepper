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
import androidx.annotation.ColorInt
import kotlin.math.absoluteValue

/**
 * Stepper View
 */
class Stepper : View {

    private var stepWidth = 0.toFloat()
    var stepHeight = 8.dpToPx(this.context)
        set(value) {
            if (value > 0) {
                field = value
                invalidate()
            }
        }
    var maxStateNumber = 1
        set(value) {
            field = value
            invalidate()
        }
    var currentStep = 1
        set(value) {
            if (value in 1..maxStateNumber) {
                field = value
                invalidate()
            }
        }
    var stepSpace = 4.dpToPx(this.context)
        set(value) {
            if (value >= 0)
            field = value
        }
    private var radius = 12.dpToPx(this.context)

    var stepFillType = StepFillType.GRADIENT
        set(value) {
            field = value
            invalidate()
        }
    var stepType = StepType.CAPSULE_START_END
        set(value) {
            field = value
            adjustRadiusByType()
        }

    @ColorInt
    private var checkedColor1: Int = Color.GREEN
    @ColorInt
    private var checkedColor2: Int = Color.RED

    @ColorInt
    private var unCheckedColor1 = Color.LTGRAY
    @ColorInt
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

        stepSpace = a.getDimension(R.styleable.Stepper_stepSpace, stepSpace)
        stepHeight = a.getDimension(R.styleable.Stepper_stepHeight, stepHeight)
        radius = a.getDimension(R.styleable.Stepper_stepCornerRadius, radius)
        stepFillType = StepFillType.values()[a.getInt(R.styleable.Stepper_stepFillType, stepFillType.ordinal)]
        stepType = StepType.values()[a.getInt(R.styleable.Stepper_stepType, stepType.ordinal)]
        maxStateNumber = a.getInt(R.styleable.Stepper_stepCount, maxStateNumber)
        currentStep = a.getInt(R.styleable.Stepper_stepSelected, currentStep).absoluteValue.takeIf { it<=maxStateNumber}?: maxStateNumber

        checkedColor1 = a.getColor(R.styleable.Stepper_stepCheckedColor, checkedColor1)
        checkedColor2 = a.getColor(R.styleable.Stepper_stepCheckedColor2, checkedColor2)
        unCheckedColor1 = a.getColor(R.styleable.Stepper_stepUnCheckedColor, unCheckedColor1)
        unCheckedColor1 = a.getColor(R.styleable.Stepper_stepUnCheckedColor2, unCheckedColor1)
        adjustRadiusByType()
        setupCorners()
        a.recycle()
    }

    private fun adjustRadiusByType() {
        if (stepType == StepType.CAPSULE || stepType == StepType.CAPSULE_START_END) {
            radius = stepHeight/2
        } else if (stepType == StepType.RECTANGLE) {
            radius = 0.toFloat()
        }
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
        val startX = (stepWidth * index) + (stepSpace * index)
        val endX = startX + stepWidth
        val startY = 0.toFloat()
        val endY = stepHeight
        val rect = RectF(
            startX,
            startY,
            endX,
            endY
        )
        val isChecked = index < currentStep
        val gradient = when(stepFillType) {
            StepFillType.GRADIENT -> {
                if (isChecked) LinearGradient(startX, startY, endX, endY, checkedColor1, checkedColor2, android.graphics.Shader.TileMode.CLAMP)
                else LinearGradient(startX, startY, endX, endY, unCheckedColor1, unCheckedColor2, android.graphics.Shader.TileMode.CLAMP)
            }
            StepFillType.NORMAL -> {
                if (isChecked) LinearGradient(startX, startY, endX, endY, checkedColor1, checkedColor1, android.graphics.Shader.TileMode.CLAMP)
                else LinearGradient(startX, startY, endX, endY, unCheckedColor1, unCheckedColor1, android.graphics.Shader.TileMode.CLAMP)
            }
        }
        return StepItem().apply {
            this.gd = gradient
            this.rect = rect
            this.checked = isChecked
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        stepWidth = (width - (stepSpace * maxStateNumber-1))/maxStateNumber
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeightSpec = MeasureSpec.makeMeasureSpec(stepHeight.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }

    fun setStepCorners(radius: Float) {
        if (radius >= 0) {
            this.radius = radius
            adjustRadiusByType()
        }
    }

    enum class StepFillType {
        GRADIENT,
        NORMAL
    }

    enum class StepType {
        CAPSULE_START_END,
        CAPSULE,
        ROUNDED_CORNER,
        RECTANGLE
    }
}