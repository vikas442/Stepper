package com.example.gpayintegration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.graphics.Shader

/**
 * TODO: document your custom view class.
 */
class Stepper : View {

    private var cellWidth = 0.toFloat()
    private var cellHeight = 24.toFloat()
    private var maxStateNumber = 3
    private var stepSpace = 10.toFloat()
    private var radius = 12.toFloat()

    private val paint = Paint()


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

        a.recycle()

    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val firstCorner = floatArrayOf(
            radius, radius,
            0f, 0f,
            0f, 0f,
            radius, radius
        )

        val lastCorner = floatArrayOf(
            0f, 0f,
            radius, radius,
            radius, radius,
            0f, 0f
        )

        val center = floatArrayOf(
            0f, 0f,
            0f, 0f,
            0f, 0f,
            0f, 0f
        )
        for (index in 0 until maxStateNumber) {
            val startX = (cellWidth * index) + (stepSpace * index)
            val endX = startX + cellWidth
            val startY = 0.toFloat()
            val endY = cellHeight

            val gd = LinearGradient(startX, startY, endX, endY, Color.parseColor("#00EAFF"), Color.parseColor("#7A17E5"), android.graphics.Shader.TileMode.CLAMP)
            paint.isDither = true
            paint.shader = gd
            val path = Path()
            val rect = RectF(
                startX,
                startY,
                endX,
                endY
            )
            when (index) {
                0 -> {
                    path.addRoundRect(
                        rect,
                        firstCorner,
                        Path.Direction.CW
                    )
                }
                maxStateNumber -1 -> {
                    path.addRoundRect(
                        rect,
                        lastCorner,
                        Path.Direction.CW
                    )
                }
                else -> {
                    path.addRoundRect(
                        rect,
                        center,
                        Path.Direction.CW
                    )
                }
            }
            canvas.drawPath(path, paint)
        }


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, cellHeight.toInt(), oldw, oldh)
        cellWidth = (width - (stepSpace * maxStateNumber-1))/maxStateNumber
    }
}