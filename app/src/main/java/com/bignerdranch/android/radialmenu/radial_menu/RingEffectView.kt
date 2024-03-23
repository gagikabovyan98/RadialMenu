package com.bignerdranch.android.radialmenu.radial_menu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import kotlin.math.cos
import kotlin.math.sin

class RingEffectView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath = Path()
    private var mAngle = 0f
        set(angle) {
            val diff = angle - mAngle
            val stepCount = (diff / STEP_DEGREE).toInt()
            val stepMod = diff % STEP_DEGREE
            val sw = mPaint.strokeWidth * 0.5f
            val radius = radius - sw
            for (i in 1..stepCount) {
                val stepAngel = mStartAngle + mAngle + STEP_DEGREE * i
                val x = cos(Math.toRadians(stepAngel.toDouble())).toFloat() * radius
                val y = sin(Math.toRadians(stepAngel.toDouble())).toFloat() * radius
                mPath.lineTo(x, y)
            }
            val stepAngel = mStartAngle + mAngle + STEP_DEGREE * stepCount + stepMod
            val x = cos(Math.toRadians(stepAngel.toDouble())).toFloat() * radius
            val y = sin(Math.toRadians(stepAngel.toDouble())).toFloat() * radius
            mPath.lineTo(x, y)
            field = angle
            invalidate()
            field = angle
        }
    private var mStartAngle = 0f
    var radius = 0

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!mPath.isEmpty) {
            canvas.save()
            canvas.translate((width / 2).toFloat(), (height / 2).toFloat())
            canvas.drawPath(mPath, mPaint)
            canvas.restore()
        }
    }

    override fun setAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        mPaint.alpha = (255 * alpha).toInt()
        invalidate()
    }

    override fun getAlpha(): Float {
        return (mPaint.alpha / 255).toFloat()
    }

    var startAngle: Float
        get() = mStartAngle
        set(startAngle) {
            mStartAngle = startAngle
            mAngle = 0f
            val sw = mPaint.strokeWidth * 0.5f
            val radius = radius - sw
            mPath.reset()
            val x = cos(Math.toRadians(startAngle.toDouble())).toFloat() * radius
            val y = sin(Math.toRadians(startAngle.toDouble())).toFloat() * radius
            mPath.moveTo(x, y)
        }

    fun setStrokeColor(color: Int) {
        mPaint.color = color
    }

    fun setStrokeWidth(width: Int) {
        mPaint.strokeWidth = width.toFloat()
    }

    companion object {
        private const val STEP_DEGREE = 8
    }
}
