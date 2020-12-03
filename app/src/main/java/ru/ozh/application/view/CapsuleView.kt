package ru.ozh.application.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import ru.ozh.application.R
import ru.ozh.application.utils.UiUtils.dp

class CapsuleView @JvmOverloads constructor(
    context: Context,
    val attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    private val paint: Paint = Paint()
        .apply {
            isAntiAlias = true
        }

    private val capsuleWidth = 16f.dp()
    private val capsuleHeight = 32f.dp()
    private val radius = 15f.dp()
    private val strokeWidth = 4f.dp()

    init {
        initAttrs()
        background = ResourcesCompat.getDrawable(resources, R.drawable.bg_capsule_btn_borderless, context.theme)
        paint.strokeWidth = strokeWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val left = centerX - (capsuleWidth / 2)
        val right = centerX + (capsuleWidth / 2)
        val top = centerY - (capsuleHeight / 2)
        val bottom = centerY + (capsuleHeight / 2)
        canvas.drawRoundRect(left, top, right, bottom, radius, radius, paint)
    }

    private fun initAttrs() {
        context.withStyledAttributes(attributeSet, R.styleable.CapsuleView) {
            val isStroke = getBoolean(R.styleable.CapsuleView_cv_is_stroke, false)
            val color = getColor(R.styleable.CapsuleView_cv_color, Color.WHITE)

            paint.color = color
            paint.style = if(isStroke) Paint.Style.STROKE else Paint.Style.FILL
        }
    }
}