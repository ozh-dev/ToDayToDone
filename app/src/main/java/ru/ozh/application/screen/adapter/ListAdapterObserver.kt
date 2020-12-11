package ru.ozh.application.screen.adapter

import androidx.recyclerview.widget.RecyclerView

class ListAdapterObserver(
        private val onChanged: (() -> Unit)? = null ,
        private val onItemRangeChanged: ((positionStart: Int, itemCount: Int) -> Unit)? = null ,
        private val onItemRangeInserted: ((positionStart: Int, itemCount: Int) -> Unit)? = null ,
        private val onItemRangeRemoved: ((positionStart: Int, itemCount: Int) -> Unit)? = null ,
        private val onItemRangeMoved: ((fromPosition: Int, toPosition: Int, itemCount: Int) -> Unit)? = null ,
) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
        onChanged?.invoke()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        onItemRangeChanged?.invoke(positionStart, itemCount)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        onItemRangeInserted?.invoke(positionStart, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        onItemRangeRemoved?.invoke(positionStart, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        onItemRangeMoved?.invoke(fromPosition, toPosition, itemCount)
    }
}

fun RecyclerView.Adapter<out RecyclerView.ViewHolder>.doOnItemRangeInserted(
        onItemRangeInserted: ((positionStart: Int, itemCount: Int) -> Unit)
) {
    registerAdapterDataObserver(ListAdapterObserver(onItemRangeInserted = onItemRangeInserted))
}