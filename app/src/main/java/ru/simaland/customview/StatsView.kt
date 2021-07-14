package ru.simaland.customview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.random.Random


class StatsView constructor(context: Context, attributes: AttributeSet? = null) :
    View(context, attributes) {

    private var lineWidth = AndroidUtils.dp(context, 15F)
    private var center = PointF(0F, 0F)
    private var radius = 0F
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.MITER
    }

    private var progress = 0F
    private var animator: Animator? = null


    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            update()
        }

    private fun update() {

        animator?.let {
            it.removeAllListeners()
            it.cancel()
        }
        progress = 0F

        animator = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            duration = 2_000
            interpolator = AccelerateInterpolator()
            start()
        }

    }

    private var oval = RectF(0F, 0F, 0F, 0F)

    init {
        context.withStyledAttributes(attributes, R.styleable.StatsView) {
            textPaint.textSize = getDimension(
                R.styleable.StatsView_textSize,
                AndroidUtils.dp(context, 50F).toFloat()
            )
            paint.strokeWidth = getDimension(
                R.styleable.StatsView_lineWidth,
                lineWidth.toFloat()
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        center = PointF(w / 2F, h / 2F)
        radius = w.coerceAtMost(h) / 2F - lineWidth / 2F
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) return

        val dataInPercent: MutableList<Float> = mutableListOf()

        data.sum().let { sum ->
            data.forEach {
                dataInPercent.add(it / sum)
            }
        }

        var startAngle = 0F
        var firstColor: Int? = null
        dataInPercent.forEach {
            val angle = it * 360F
            paint.color = 0xFF005678.toInt()
            if (firstColor == null) {
                firstColor = paint.color
            }
            canvas.drawArc(oval, (startAngle) * progress, angle, false, paint)
            startAngle += angle
        }

        canvas.drawText(
            "%.2f%%".format(dataInPercent.sum() * 100),
            center.x,
            center.y - textPaint.textSize / 6,
            textPaint
        )
    }
}