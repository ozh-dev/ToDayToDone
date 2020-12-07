package ru.ozh.application.utils

import android.content.res.Resources
import android.util.DisplayMetrics

object UiUtils {

    /**
     * Convert dp to px
     * @return px
     */
    fun convertDpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /***
     * Convert float value to px value
     */
    fun Float.dp(): Float {
        return convertDpToPixel(this)
    }
}