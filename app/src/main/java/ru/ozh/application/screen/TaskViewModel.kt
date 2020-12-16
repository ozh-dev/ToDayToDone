package ru.ozh.application.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ozh.application.domain.Priority
import ru.ozh.application.domain.Task
import java.util.*

class TaskViewModel : ViewModel() {

    //region params
    val tasks: MutableLiveData<List<Task>> = MutableLiveData<List<Task>>()
    private var sortPriority: Priority = Priority.UNKNOWN
    private val internalTasks: MutableList<Task> = mutableListOf()
    //endregion

    fun onTaskTask(priority: Priority, text: String) {
        val task = Task(
            id = UUID.randomUUID().toString(),
            text = text,
            priority = priority
        )
        internalTasks.add(task)
        emit()
    }

    fun onSwipe(adapterPosition: Int, swipeAction: SwipeAction) {
        when(swipeAction) {
            SwipeAction.REMOVE -> removeTaskAt(adapterPosition)
            SwipeAction.DONE -> doneTaskAt(adapterPosition)
            SwipeAction.UNKNOWN -> Unit
        }
    }

    fun onSortChange(priority: Priority) {
        sortPriority = priority
        emit()
    }

    private fun removeTaskAt(position: Int) {
        internalTasks.removeAt(position)
        emit()
    }

    private fun doneTaskAt(position: Int) {
        val task = internalTasks[position]
        val taskStatus = task.hasDone
        val updatedTask = task.copy(hasDone = !taskStatus)
        internalTasks[position] = updatedTask
        emit()
    }

    private fun emit() {
        val tempList = internalTasks.sortedByDescending { it.priority.key == sortPriority.key }
        internalTasks.clear()
        internalTasks.addAll(tempList)
        tasks.postValue(
                mutableListOf<Task>()
                        .apply {
                            addAll(internalTasks)
                        }
        )
    }
}