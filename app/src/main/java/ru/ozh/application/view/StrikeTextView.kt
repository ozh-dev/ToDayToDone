package ru.ozh.application.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import ru.ozh.application.R
import ru.ozh.application.utils.UiUtils.dp


class StrikeTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    //region params
    var hasStriked = false
        set(value) {
            if (field == value) return
            field = value
            invalidate()
        }

    private val paint = Paint()
            .apply {
                isAntiAlias = true
                color = ContextCompat.getColor(context, R.color.text_2)
                strokeWidth = 2f.dp()
            }
    //endregion

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        hasStriked = false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(hasStriked) {
            canvas.drawLine(
                    0f,
                    height / 2f,
                    calcTextWidth(),
                    height / 2f,
                    paint
            )
        }
    }

    private fun calcTextWidth(): Float {
        return getPaint().measureText(text.toString())
    }
}