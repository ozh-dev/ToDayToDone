package ru.ozh.application.domain

import androidx.annotation.ColorRes
import ru.ozh.application.R

object Common {

    @ColorRes
    fun getPriorityColorRes(priority: Priority): Int {
        return when(priority) {
            Priority.LOW -> R.color.gray
            Priority.MIDDLE -> R.color.petroleum_blue
            Priority.HIGH -> R.color.red
            Priority.UNKNOWN -> R.color.gray
        }
    }
}