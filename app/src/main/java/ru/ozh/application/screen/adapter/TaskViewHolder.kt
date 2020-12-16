package ru.ozh.application.screen.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.ozh.application.R
import ru.ozh.application.domain.Task
import ru.ozh.application.view.PriorityView
import ru.ozh.application.view.StrikeTextView

class TaskViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val priorityView = itemView.findViewById<PriorityView>(R.id.priority_view)
    private val textTv = itemView.findViewById<StrikeTextView>(R.id.text_tv)

    fun bind(task: Task) {
        textTv.hasStriked = task.hasDone
        textTv.text = task.text
        priorityView.priority = task.priority
    }
}
