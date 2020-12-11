package ru.ozh.application.screen

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import ru.ozh.application.R
import ru.ozh.application.utils.UiUtils.dp
import kotlin.math.abs


class SwipeController(
        private val context: Context,
        private val callback: (adapterPosition: Int, swipeAction: SwipeAction) -> Unit
) : Callback() {

    private val iconOffset = 16f.dp()
    private val threshold = 72f.dp()
    private val cornerRadius = 50f.dp()
    private val redColor = context.getColor(R.color.red_1)
    private val greenColor = context.getColor(R.color.green)
    private val doneIconBtm = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_check, null)?.toBitmap()
    private val removeIconBtm = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_remove, null)?.toBitmap()
    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var adapterPosition: Int = RecyclerView.NO_POSITION
    private var swipeAction: SwipeAction = SwipeAction.UNKNOWN

    override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = START or END
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        when (actionState) {
            ACTION_STATE_IDLE -> {
                if (swipeAction != SwipeAction.UNKNOWN) {
                    callback(adapterPosition, swipeAction)
                }
                adapterPosition = RecyclerView.NO_POSITION
                swipeAction = SwipeAction.UNKNOWN
            }
            ACTION_STATE_SWIPE -> {
                adapterPosition = viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION
            }
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 1f
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Float.MAX_VALUE
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //do nothing
    }

    override fun onChildDraw(
            canvas: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {
        val isLtr = 0 < dX

        val deltaX = if (isLtr) {
            dX.coerceIn(0f, threshold)
        } else {
            dX.coerceIn(-threshold, 0f)
        }

        if (isLtr) {
            if (abs(deltaX) == threshold && isCurrentlyActive) {
                swipeAction = SwipeAction.REMOVE
            }
            drawRemove(canvas, viewHolder.itemView, deltaX.toInt())
        } else {
            if (abs(deltaX) == threshold && isCurrentlyActive) {
                swipeAction = SwipeAction.DONE
            }
            drawDone(canvas, viewHolder.itemView, deltaX.toInt())
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, deltaX, dY, actionState, isCurrentlyActive)
    }

    private fun drawDone(canvas: Canvas, viewItem: View, dx: Int) {
        val rect = getRightRect(viewItem, dx)
        drawBackground(canvas, isRtl = true, greenColor, rect)
        val left = rect.left
        val top = rect.top
        drawIcon(canvas, doneIconBtm, left + iconOffset, top + iconOffset)
    }

    private fun drawRemove(canvas: Canvas, viewItem: View, dx: Int) {
        val rect = getLeftRect(viewItem, dx)
        drawBackground(canvas, isRtl = false, redColor, rect)
        val iconWidth = removeIconBtm?.width ?: 0
        val right = rect.right
        val top = rect.top
        drawIcon(canvas, removeIconBtm, right - iconWidth - iconOffset, top + iconOffset)
    }

    private fun drawIcon(canvas: Canvas, bitmap: Bitmap?, left: Float, top: Float) {
        bitmap?.let { canvas.drawBitmap(it, left, top, null) }
    }

    private fun drawBackground(canvas: Canvas, isRtl: Boolean, color: Int, rect: RectF) {
        path.reset()
        path.addRoundRect(rect, getCornersArray(isRtl = isRtl), Path.Direction.CW)
        paint.color = color
        canvas.drawPath(path, paint)
    }

    private fun getRightRect(viewItem: View, dx: Int): RectF {
        val leftBound = viewItem.right.toFloat() + dx
        val rightBound = viewItem.right.toFloat()
        val topBound = viewItem.top.toFloat()
        val bottomBound = viewItem.bottom.toFloat()
        return RectF(leftBound, topBound, rightBound, bottomBound)
    }

    private fun getLeftRect(viewItem: View, dx: Int): RectF {
        val rightBound = viewItem.left.toFloat() + dx
        val leftBound = viewItem.left.toFloat()
        val topBound = viewItem.top.toFloat()
        val bottomBound = viewItem.bottom.toFloat()
        return RectF(leftBound, topBound, rightBound, bottomBound)
    }

    private fun getCornersArray(isRtl: Boolean): FloatArray {
        return if (isRtl) {
            floatArrayOf(
                    cornerRadius, cornerRadius,   // Верхний левый в px
                    0f, 0f,                       // Верхний правый в px
                    0f, 0f,                       // Нижний правай в px
                    cornerRadius, cornerRadius    // Нижний левай в px
            )
        } else {
            floatArrayOf(
                    0f, 0f,                       // Верхний левый в px
                    cornerRadius, cornerRadius,   // Верхний правый в px
                    cornerRadius, cornerRadius,   // Нижний правай в px
                    0f, 0f                        // Нижний левай в px
            )
        }
    }
}