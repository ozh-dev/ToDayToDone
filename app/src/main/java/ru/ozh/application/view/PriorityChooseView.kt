package ru.ozh.application.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import ru.ozh.application.R
import ru.ozh.application.utils.UiUtils.dp
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.sin


class PriorityChooseView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    //region params
    private val closeBtn: ImageButton
    private val ignoreTag = context.resources.getString(R.string.ignore_tag)
    //endregion

    init {
        inflate(context, R.layout.layout_color_choice, this)
        background = ContextCompat.getDrawable(context, R.drawable.bg_capsule_btn_shape)
        elevation = 8f.dp()
        closeBtn = findViewById(R.id.close_btn)

        closeBtn.setOnClickListener {
            hide()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = max(width, height)
        setMeasuredDimension(size, size)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        children.filter { it.tag != ignoreTag }
            .forEachIndexed { index, view ->
                view.updateLayoutParams<LayoutParams> {
                    gravity = Gravity.CENTER
                }
                place(view, index, (width.times(0.3f)).toInt())
            }
    }

    fun show() {
        createRevealAnimation(isReverse = false)
            .apply { doOnStart { isVisible = true } }
            .start()
    }

    fun hide() {
        createRevealAnimation(isReverse = true)
            .apply { doOnEnd { isVisible = false } }
            .start()
    }

    private fun createRevealAnimation(isReverse: Boolean): Animator {
        val (layoutWidth, layoutHeight) = layoutParams.width to layoutParams.height
        val cx = layoutWidth / 2
        val cy = layoutHeight / 2

        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        return ViewAnimationUtils.createCircularReveal(
            this,
            cx,
            cy,
            if (isReverse) finalRadius else 0f,
            if (isReverse) 0f else finalRadius
        )
    }

    private fun place(child: View, position: Int, radius: Int) {
        val angleDeg = 270 + (position * 45.0)

        val angleRad = toRadians(angleDeg)
        val x = radius.times(cos(angleRad).toFloat())
        val y = radius.times(sin(angleRad).toFloat())

        child.rotation = position.times(45f)
        child.translationX = x
        child.translationY = y
    }
}