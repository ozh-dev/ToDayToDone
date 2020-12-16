package ru.ozh.application.screen

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import ru.ozh.application.R
import ru.ozh.application.domain.Priority
import ru.ozh.application.screen.adapter.TasksAdapter
import ru.ozh.application.screen.adapter.doOnItemRangeInserted
import ru.ozh.application.utils.UiUtils.dp
import ru.ozh.application.view.PriorityChooseView
import ru.ozh.application.view.PriorityView

class TasksActivity : AppCompatActivity() {

    //region params
    private val tasksAdapter = TasksAdapter()
    private val vm: TaskViewModel by viewModels()

    private lateinit var tasksRv: RecyclerView
    private lateinit var addBtn: Button
    private lateinit var sortBtn: ImageButton
    private lateinit var inputEditText: TextInputEditText
    private lateinit var openPriorityPickerBtn: PriorityView
    private lateinit var lowPriorityPickerBtn: PriorityView
    private lateinit var middlePriorityPickerBtn: PriorityView
    private lateinit var highPriorityPickerBtn: PriorityView
    private lateinit var priorityPickerView: PriorityChooseView
    private lateinit var popupWindow: PopupWindow
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
        initViews()
        initPopupWindows()
        initPriorityButtons()
        initAddBar()
        initRecyclerView()
        subscribeViewModel()

    }

    private fun initViews() {
        tasksRv = findViewById(R.id.tasks_rv)
        addBtn = findViewById(R.id.add_task_btn)
        sortBtn = findViewById(R.id.sort_btn)
        inputEditText = findViewById(R.id.task_input_et)
        openPriorityPickerBtn = findViewById(R.id.open_priority_btn)
        lowPriorityPickerBtn = findViewById(R.id.low_priority_btn)
        middlePriorityPickerBtn = findViewById(R.id.middle_priority_btn)
        highPriorityPickerBtn = findViewById(R.id.high_priority_btn)
        priorityPickerView = findViewById(R.id.color_choose_view)
    }

    private fun initPopupWindows() {
        popupWindow = PopupWindow(this)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_sort, null, false)
        popupWindow.contentView = view
        popupWindow.width = 120f.dp().toInt()
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_rect_round_12))

        view.findViewById<ViewGroup>(R.id.low_sort_btn).setOnClickListener {
            vm.onSortChange(Priority.LOW)
            popupWindow.dismiss()
        }

        view.findViewById<ViewGroup>(R.id.middle_sort_btn).setOnClickListener {
            vm.onSortChange(Priority.MIDDLE)
            popupWindow.dismiss()
        }

        view.findViewById<ViewGroup>(R.id.high_sort_btn).setOnClickListener {
            vm.onSortChange(Priority.HIGH)
            popupWindow.dismiss()
        }

        sortBtn.setOnClickListener {
            popupWindow.showAsDropDown(sortBtn)
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

    private fun initAddBar() {
        addBtn.setOnClickListener {

            val isEmpty = inputEditText.text?.isEmpty() == true

            if (isEmpty) {
                showEmptyTaskMessage()
                return@setOnClickListener
            }

            vm.onTaskTask(
                    priority = openPriorityPickerBtn.priority,
                    text = inputEditText.text.toString()
            )
            inputEditText.editableText.clear()
        }

        openPriorityPickerBtn.setOnClickListener {
            priorityPickerView.show()
        }
    }

    private fun initRecyclerView() {
        with(tasksRv) {
            layoutManager = LinearLayoutManager(this@TasksActivity)
            adapter = tasksAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            val swipeController = SwipeController(this@TasksActivity
            ) { adapterPosition, swipeAction ->
                postDelayed({
                    vm.onSwipe(adapterPosition, swipeAction)
                }, 250)
            }
            ItemTouchHelper(swipeController).attachToRecyclerView(this)
            tasksRv.adapter?.doOnItemRangeInserted(this@TasksActivity::scrollToBottom)
        }
    }

    private fun subscribeViewModel() {
        vm.tasks.observe(this, tasksAdapter::submitList)
    }

    private fun showEmptyTaskMessage() {
        Snackbar.make(inputEditText, R.string.input_task_message, Snackbar.LENGTH_SHORT)
                .apply {
                    setAnchorView(R.id.add_bar_layout)
                    show()
                }
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