package ru.ozh.application.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ozh.application.domain.Priority
import ru.ozh.application.domain.Task
import java.util.*

class TaskViewModel : ViewModel() {

    val tasks: MutableLiveData<List<Task>> = MutableLiveData<List<Task>>()
    private val internalTasks: MutableList<Task> = mutableListOf()

    fun addTask(priority: Priority, text: String) {
        val task = Task(
            id = UUID.randomUUID().toString(),
            text = text,
            priority = priority
        )
        internalTasks.add(0, task)
        tasks.postValue(
            mutableListOf<Task>()
                .apply {
                    addAll(internalTasks)
                }
        )
    }
}