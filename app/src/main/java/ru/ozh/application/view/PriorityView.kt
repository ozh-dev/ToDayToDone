package ru.ozh.application.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import ru.ozh.application.R
import ru.ozh.application.domain.Common.getPriorityColorRes
import ru.ozh.application.domain.Priority
import ru.ozh.application.domain.Priority.*
import ru.ozh.application.utils.UiUtils.dp

class PriorityView @JvmOverloads constructor(
    context: Context,
    private val attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    //region params
    var priority: Priority = UNKNOWN
        set(value) {
            field = value
            paint.color = context.getColor(getPriorityColorRes(field))
            invalidate()
        }

    private val paint: Paint = Paint()
        .apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

    private val capsuleWidth = 10f.dp()
    private val capsuleHeight = 24f.dp()
    private val radius = 15f.dp()
    //endregion

    init {
        initAttrs()
        background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.bg_capsule_btn_borderless,
            context.theme
        )
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
            val priorityValue = getInt(R.styleable.CapsuleView_cv_priority, Priority.UNKNOWN.ordinal)
            priority = Priority.values()[priorityValue]
        }
    }
}