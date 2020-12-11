package ru.ozh.application.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ozh.application.domain.Priority
import ru.ozh.application.domain.Task
import java.text.FieldPosition
import java.util.*

class TaskViewModel : ViewModel() {

    val tasks: MutableLiveData<List<Task>> = MutableLiveData<List<Task>>()
    private val internalTasks: MutableList<Task> = mutableListOf()

    init {
        internalTasks.add(
            Task(
                id = UUID.randomUUID().toString(),
                text = "123456",
                priority = Priority.HIGH
            )
        )

        internalTasks.add(
            Task(
                id = UUID.randomUUID().toString(),
                text = "456789",
                priority = Priority.LOW
            )
        )

        internalTasks.add(
            Task(
                id = UUID.randomUUID().toString(),
                text = "789123",
                priority = Priority.MIDDLE
            )
        )
    }

    fun addTask(priority: Priority, text: String) {
        val task = Task(
            id = UUID.randomUUID().toString(),
            text = text,
            priority = priority
        )
        internalTasks.add(task)
        emit()
    }

    private fun removeTask(position: Int) {
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
        tasks.postValue(
                mutableListOf<Task>()
                        .apply {
                            addAll(internalTasks)
                        }
        )
    }

    fun doTaskAction(adapterPosition: Int, swipeAction: SwipeAction) {
        when(swipeAction) {
            SwipeAction.REMOVE -> removeTask(adapterPosition)
            SwipeAction.DONE -> doneTaskAt(adapterPosition)
            SwipeAction.UNKNOWN -> Unit
        }
    }
}