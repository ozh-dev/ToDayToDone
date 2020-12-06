package ru.ozh.application.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import ru.ozh.application.utils.UiUtils.dp
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.sin


class ColorChooseView @JvmOverloads constructor(
        context: Context,
        val attributeSet: AttributeSet? = null,
        defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

//    private val paint: Paint = Paint()
//            .apply {
//                isAntiAlias = true
//                color = Color.WHITE
//            }

    init {

        setOnClickListener {
            show()
        }
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)

//        val cX = width / 2f
//        val cY = height / 2f
//        val radius = width / 2f
//        paint.setShadowLayer(radius / 15, 0f, 0f, Color.BLACK)
//        canvas.drawCircle(cX, cY, radius, paint)
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = max(width, height)
        setMeasuredDimension(size, size)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        children.filter { it.tag != "ignore" }.forEachIndexed { index, view ->
            view.updateLayoutParams<FrameLayout.LayoutParams> {
                gravity = Gravity.CENTER
            }
            place(view, index, (width * 0.3f).toInt())
        }
    }

    private fun show() {
        val cx = (width / 2) //- 20f.dp()
        val cy = (height / 2) //+ 20f.dp()

        // get the final radius for the clipping circle
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(this, cx.toInt(), cy.toInt(), 20f.dp(), finalRadius)
        anim.duration = 500
//        anim.interpolator = OvershootInterpolator()
        visibility = View.VISIBLE
        anim.start()
    }

    private fun place(child: View, position: Int, radius: Int) {
        val angleDeg = 270 + (position * 45.0)

        val angleRad = toRadians(angleDeg)
        val x = radius * cos(angleRad).toFloat()
        val y = radius * sin(angleRad).toFloat()

        child.rotation = position * 45f
        child.translationX = x
        child.translationY = y
    }
}