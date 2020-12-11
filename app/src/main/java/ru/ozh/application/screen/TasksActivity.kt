package ru.ozh.application.screen

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.PopupWindowCompat
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import ru.ozh.application.R
import ru.ozh.application.screen.adapter.TasksAdapter
import ru.ozh.application.screen.adapter.doOnItemRangeInserted
import ru.ozh.application.view.PriorityChooseView
import ru.ozh.application.view.PriorityView

class TasksActivity : AppCompatActivity() {

    private val tasksAdapter = TasksAdapter()
    private val vm: TaskViewModel by viewModels()

    private lateinit var tasksRv: RecyclerView
    private lateinit var addBtn: Button
    private lateinit var inputEditText: TextInputEditText
    private lateinit var openPriorityPickerBtn: PriorityView
    private lateinit var lowPriorityPickerBtn: PriorityView
    private lateinit var middlePriorityPickerBtn: PriorityView
    private lateinit var highPriorityPickerBtn: PriorityView
    private lateinit var priorityPickerView: PriorityChooseView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
        initViews()
        initPriorityButtons()
        initAddBar()
        initRecyclerView()
        subscribeViewModel()
    }

    private fun initViews() {
        tasksRv = findViewById(R.id.tasks_rv)
        addBtn = findViewById(R.id.add_task_btn)
        inputEditText = findViewById(R.id.task_input_et)
        openPriorityPickerBtn = findViewById(R.id.open_priority_btn)
        lowPriorityPickerBtn = findViewById(R.id.low_priority_btn)
        middlePriorityPickerBtn = findViewById(R.id.middle_priority_btn)
        highPriorityPickerBtn = findViewById(R.id.high_priority_btn)
        priorityPickerView = findViewById(R.id.color_choose_view)
    }

    private fun subscribeViewModel() {
        vm.tasks.observe(this, tasksAdapter::submitList)
    }

    private fun initRecyclerView() {
        with(tasksRv) {
            layoutManager = LinearLayoutManager(this@TasksActivity)
            adapter = tasksAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            val swipeController = SwipeController(this@TasksActivity
            ) { adapterPosition, swipeAction ->
                postDelayed({
                    vm.doTaskAction(adapterPosition, swipeAction)
                }, 250)
            }
            ItemTouchHelper(swipeController).attachToRecyclerView(this)
            tasksRv.adapter?.doOnItemRangeInserted(this@TasksActivity::scrollToBottom)
        }
    }

    private fun initAddBar() {
        addBtn.setOnClickListener {

            val isEmpty = inputEditText.text?.isEmpty() == true

            if(isEmpty) {
                showEmptyTaskMessage()
                return@setOnClickListener
            }

            vm.addTask(
                    priority = openPriorityPickerBtn.priority,
                    text = inputEditText.text.toString()
            )
            inputEditText.editableText.clear()
        }

        openPriorityPickerBtn.setOnClickListener {
            priorityPickerView.show()
        }
    }

    private fun showEmptyTaskMessage() {
        Snackbar.make(inputEditText, R.string.input_task_message, Snackbar.LENGTH_SHORT)
                .apply {
                    setAnchorView(R.id.add_bar_layout)
                    show()
                }
    }

    private fun initPriorityButtons() {
        val priorityClickListener = View.OnClickListener { view ->
            if (view is PriorityView) {
                openPriorityPickerBtn.priority = view.priority
                priorityPickerView.hide()
            }
        }

        lowPriorityPickerBtn.setOnClickListener(priorityClickListener)
        middlePriorityPickerBtn.setOnClickListener(priorityClickListener)
        highPriorityPickerBtn.setOnClickListener(priorityClickListener)
    }

    private fun initPopupWindows() {
        val popupWindow = PopupWindow(this)
//        popupWindow.setContentView()
    }

    private fun scrollToBottom(positionStart: Int, itemCount: Int) {
        if ((tasksRv.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() != positionStart - 1) return

        val scroller = object : LinearSmoothScroller(this@TasksActivity) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 300f / displayMetrics.densityDpi
            }
        }

        scroller.targetPosition = positionStart + 1
        tasksRv.layoutManager?.startSmoothScroll(scroller)
    }
}