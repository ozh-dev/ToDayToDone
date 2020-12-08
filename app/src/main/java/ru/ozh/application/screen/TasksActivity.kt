package ru.ozh.application.screen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import ru.ozh.application.R
import ru.ozh.application.screen.adapter.TasksAdapter
import ru.ozh.application.view.PriorityChooseView
import ru.ozh.application.view.PriorityView

class TasksActivity : AppCompatActivity() {

    private val tasksAdapter = TasksAdapter()
    private val vm: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        val tasksRv = findViewById<RecyclerView>(R.id.tasks_rv)
        val addBtn = findViewById<Button>(R.id.add_task_btn)
        val inputEditText = findViewById<TextInputEditText>(R.id.task_input_et)
        val openPriorityPickerBtn = findViewById<PriorityView>(R.id.open_priority_btn)
        val lowPriorityPickerBtn = findViewById<PriorityView>(R.id.low_priority_btn)
        val middlePriorityPickerBtn = findViewById<PriorityView>(R.id.middle_priority_btn)
        val highPriorityPickerBtn = findViewById<PriorityView>(R.id.high_priority_btn)
        val priorityPickerView = findViewById<PriorityChooseView>(R.id.color_choose_view)

        val priorityClickListener = View.OnClickListener { view ->
                if(view is PriorityView) {
                    openPriorityPickerBtn.priority = view.priority
                    priorityPickerView.hide()
                }
        }

        lowPriorityPickerBtn.setOnClickListener(priorityClickListener)
        middlePriorityPickerBtn.setOnClickListener(priorityClickListener)
        highPriorityPickerBtn.setOnClickListener(priorityClickListener)

        addBtn.setOnClickListener {
            vm.addTask(
                priority = openPriorityPickerBtn.priority,
                text = inputEditText.text.toString()
            )
            inputEditText.editableText.clear()
        }

        openPriorityPickerBtn.setOnClickListener {
            priorityPickerView.show()
        }

        with(tasksRv) {
            layoutManager = LinearLayoutManager(this@TasksActivity)
            adapter = tasksAdapter
//            tasksRv.itemAnimator = TodoAnimator()

        }

        vm.tasks.observe(this, { tasks ->
            tasksAdapter.submitList(tasks)
        })

//        tasksRv.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                super.onItemRangeInserted(positionStart, itemCount)
//
//                if ((tasksRv.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() != 0) return
//
//                val scroller = object : LinearSmoothScroller(this@TasksActivity) {
//                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
//                        return 300f / displayMetrics.densityDpi
//                    }
//                }
//
//                scroller.targetPosition = 0
//                tasksRv.layoutManager?.startSmoothScroll(scroller)
//            }
//        })
    }
}

class TodoAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val view = holder.itemView
        val animation = view.animate()
        animation.alpha(1f)
            .setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationStart(animation: Animator?) {
                    view.alpha = 0f
                }

                override fun onAnimationCancel(animator: Animator) {
                    view.alpha = 1f
                }

                override fun onAnimationEnd(animator: Animator) {
                    animation.setListener(null)
                }
            }
            )
            .start()
        return true
    }
}